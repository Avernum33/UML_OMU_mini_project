import { deflateRawSync } from "node:zlib";
import { mkdir, readFile, readdir, writeFile } from "node:fs/promises";
import path from "node:path";

const SOURCE_DIR = path.resolve("diagrams", "plantuml");
const OUTPUT_DIR = path.resolve("diagrams", "rendered");
const SERVER_URL = "https://www.plantuml.com/plantuml/svg";

function encode6bit(value) {
  if (value < 10) {
    return String.fromCharCode(48 + value);
  }
  value -= 10;
  if (value < 26) {
    return String.fromCharCode(65 + value);
  }
  value -= 26;
  if (value < 26) {
    return String.fromCharCode(97 + value);
  }
  value -= 26;
  if (value === 0) {
    return "-";
  }
  if (value === 1) {
    return "_";
  }
  return "?";
}

function append3bytes(byte1, byte2, byte3) {
  const char1 = byte1 >> 2;
  const char2 = ((byte1 & 0x3) << 4) | (byte2 >> 4);
  const char3 = ((byte2 & 0xf) << 2) | (byte3 >> 6);
  const char4 = byte3 & 0x3f;

  return (
    encode6bit(char1 & 0x3f) +
    encode6bit(char2 & 0x3f) +
    encode6bit(char3 & 0x3f) +
    encode6bit(char4 & 0x3f)
  );
}

function encodePlantUml(text) {
  const compressed = deflateRawSync(Buffer.from(text, "utf8"));
  let encoded = "";

  for (let index = 0; index < compressed.length; index += 3) {
    if (index + 2 === compressed.length) {
      encoded += append3bytes(compressed[index], compressed[index + 1], 0);
    } else if (index + 1 === compressed.length) {
      encoded += append3bytes(compressed[index], 0, 0);
    } else {
      encoded += append3bytes(compressed[index], compressed[index + 1], compressed[index + 2]);
    }
  }

  return encoded;
}

function hasPlantUmlBoundaries(source) {
  return source.includes("@startuml") && source.includes("@enduml");
}

async function renderDiagram(fileName) {
  const sourcePath = path.join(SOURCE_DIR, fileName);
  const source = await readFile(sourcePath, "utf8");

  if (!hasPlantUmlBoundaries(source)) {
    throw new Error(`${fileName} is missing @startuml or @enduml`);
  }

  const encoded = encodePlantUml(source);
  const response = await fetch(`${SERVER_URL}/${encoded}`);
  const svg = await response.text();

  if (!response.ok || !svg.includes("<svg")) {
    throw new Error(`PlantUML render failed for ${fileName}: ${response.status} ${response.statusText}`);
  }

  const outputName = fileName.replace(/\.puml$/i, ".svg");
  await writeFile(path.join(OUTPUT_DIR, outputName), svg, "utf8");
  return outputName;
}

async function main() {
  await mkdir(OUTPUT_DIR, { recursive: true });

  const files = (await readdir(SOURCE_DIR))
    .filter((fileName) => fileName.endsWith(".puml"))
    .sort();

  for (const fileName of files) {
    const outputName = await renderDiagram(fileName);
    console.log(`Rendered ${fileName} -> diagrams/rendered/${outputName}`);
  }
}

main().catch((error) => {
  console.error(error);
  process.exit(1);
});

