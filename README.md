# OMU Mini Project - Drinks and Snack Ordering Kiosk

This repository contains a structured UML and Java skeleton solution for the APU campus-wide drinks and snack ordering kiosk system.

## Project Structure

- `docs/` - assignment extract, requirements analysis, report draft, and validation notes.
- `diagrams/plantuml/` - UML source files in PlantUML format.
- `src/main/java/` - Java skeletal implementation of the selected design patterns.
- `src/test/java/` - simple executable scenario checks without external test frameworks.

## Format Choice

PlantUML (`.puml`) is used instead of `.uxf` because it is easier to review, version, diff, and keep consistent across diagrams. The files can be rendered with the PlantUML VS Code extension, IntelliJ PlantUML plugin, or the PlantUML CLI.

## Assignment Coverage

- High-level use case diagram with descriptions.
- Initial class diagram without design patterns.
- Three sequence diagrams for core use cases.
- Refined class diagram using three design patterns.
- Pattern interaction sequence diagrams.
- Java skeletal implementation of the design patterns.
- Report draft with evaluation and critical appraisal.

## Suggested Work Order

1. Read `docs/00-assignment-extract.md`.
2. Review `docs/01-requirements-analysis.md`.
3. Inspect the UML diagrams in `diagrams/plantuml/`.
4. Review the Java skeleton under `src/main/java/`.
5. Use `docs/report.md` as the report base.

## Build Note

The code is plain Java and has no external dependencies. A local JDK is required to compile it.

```bash
javac -d out $(find src/main/java src/test/java -name "*.java")
java -cp out omu.PatternScenarioTest
```

On Windows PowerShell:

```powershell
Get-ChildItem -Recurse src/main/java,src/test/java -Filter *.java | ForEach-Object { $_.FullName } > sources.txt
javac -d out @sources.txt
java -cp out omu.PatternScenarioTest
```
