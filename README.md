# Ticketverkauf – Multithreaded Event Ticketing System

Nebenläufiges Softwaredesign trifft auf Eventmanagement – Eine Java-basierte Ticketverkaufsanwendung mit Fokus auf threadsichere Datenverarbeitung und Nebenläufigkeit (Concurrency).

**Live Repository**  
https://github.com/Laurita003/Ticketverkauf

---

## Features

* **Nebenläufigkeit & Synchronisation**
  * Threadsichere Ticketgenerierung und -reservierung
  * Vermeidung von Race Conditions bei zeitgleichen Buchungen
  * Sperrmechanismen (Locks) und atomare Operationen

* **Ticket-Verwaltung**
  * Dynamische Erstellung von Events und Sitzplatzkategorien
  * Verfügbarkeitsprüfung in Echtzeit
  * Automatisierte Validierung des Buchungsprozesses

* **Datenverarbeitung**
  * Schnelles Zustandsmanagement für aktive Transaktionen
  * Strukturierte Fehlerbehandlung bei Überbuchungsszenarien
  * Protokollierung von Transaktionshistorien

---

## Technologien

* Java (SE)
* Java Concurrency API (`java.util.concurrent`)
* Multithreading & Synchronisationskonzepte
* Git / GitHub

---

## Autorin

**Laurita Elsa Kenfack**  
* GitHub: [@Laurita003](https://github.com/Laurita003)

---

## Ausführung

Ausführung über die Kommandozeile (CLI) / lokale JVM:

```bash
git clone [https://github.com/Laurita003/Ticketverkauf.git](https://github.com/Laurita003/Ticketverkauf.git)
cd Ticketverkauf
javac Main.java
java Main

