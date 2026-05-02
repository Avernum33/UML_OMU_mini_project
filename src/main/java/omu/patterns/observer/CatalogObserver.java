package omu.patterns.observer;

public interface CatalogObserver {
    void onCatalogChanged(CatalogEvent event);
}

