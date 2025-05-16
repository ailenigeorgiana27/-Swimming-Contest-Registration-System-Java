# 🏊 Swimming Contest Registration System

## 📘 Overview

This application is used by the organizers of a national swimming contest to register participants from various local offices. It is implemented in **two interchangeable versions**:

- **Java server + C# clients**
- **C# server + Java clients**

Communication between clients and server is done via **multithreaded TCP sockets**, with optional **gRPC integration** for modern and efficient RPC-based interaction.

---

## 🎯 Core Features

1. **Login**
   - Office staff log into the system.
   - After login, a new window displays all available swimming events (distance & style) along with the current number of registered participants per event.

2. **Search Participants**
   - After login, staff can search for participants registered in a specific event.
   - Displayed information: participant's name, age, and number of events registered for.

3. **Register Participant**
   - A participant can register for multiple events.
   - Staff enter participant's name, age, and the selected events.
   - Once registered, all other clients across the country receive automatic updates reflecting the new data.

4. **Logout**
   - The user logs out of the system.

---

## 🏗️ Architecture

- **Client-Server** application with TCP Socket-based communication (multi-threaded)
- **Relational database** for persistent storage
- Follows the **MVC pattern**, using:
  - **Model**: domain entities (Participant, Event, Registration, etc.)
  - **Repository layer**: handles data access using SQL/ORM
  - **Service layer**: handles business logic
  - **Controller/UI layer**: interacts with services
- Connection settings are stored in a **configuration file**
- **Logging** is implemented for the repository layer

---

## 🔧 Technologies Used

### Back-End

| Java Version                         | C# Version                          |
|-------------------------------------|-------------------------------------|
| JDBC / Hibernate (ORM)              | ADO.NET / Entity Framework (ORM)    |
| Java Sockets                         | .NET Sockets                        |
| Java Properties file for config     | appsettings.json for config         |
| SLF4J / Logback for logging         | Serilog / NLog for logging          |

### Front-End

| Java Version (Client)               | C# Version (Client)                 |
|-------------------------------------|-------------------------------------|
| JavaFX UI                           | Windows Forms / WPF                 |
| MVC-based GUI controller            | MVVM/MVC-based GUI controller       |

---

## 🌐 Networking

- Implemented using low-level **TCP sockets**
- The server can handle multiple clients simultaneously using **threads**
- **Client notification mechanism** ensures that all UI instances reflect real-time updates (e.g., new registration updates participant counts instantly)

---

## 🔄 gRPC Integration (Bonus Requirement)

As a bonus enhancement, gRPC is used to modernize part of the client-server communication.

### Why gRPC?

- Enables **fast and type-safe** remote calls using Protocol Buffers
- Supports **language interoperability** (Java ↔ C#)
- Scales better than raw sockets for structured data exchange

### Implementation Notes

- A gRPC service is defined via `.proto` files (shared between client and server)
- At least two domain entities are exposed via gRPC (e.g., `ParticipantService`, `EventService`)
- Server implements the gRPC services
- Client consumes those services to:
  - Retrieve available events
  - Register participants remotely

> gRPC is used **alongside** the socket-based implementation for selected use cases, especially where binary performance and schema consistency matter.

---



