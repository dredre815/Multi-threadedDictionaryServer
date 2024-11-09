# Dictionary Client-Server Application

A Java-based dictionary application implementing a client-server architecture that allows users to query, add, remove, and update word definitions through a graphical user interface.

## Features

- **Multi-threaded Server**: Handles multiple client connections simultaneously using a custom thread pool
- **Graphical User Interface**: Both client and server components feature user-friendly GUI
- **Persistent Storage**: Dictionary data is stored in a file and persists between sessions
- **Real-time Updates**: Immediate synchronization between server and connected clients
- **Concurrent Access**: Thread-safe dictionary operations for multiple users

## Architecture

### Server Components
- `DictionaryServer`: Main server class with GUI control panel
- `Dictionary`: Core dictionary data structure and operations
- `DictionaryFileHandler`: Handles file I/O operations
- `ClientHandler`: Manages individual client connections
- `Protocol`: Implements the communication protocol
- `CustomThreadPool`: Manages worker threads for client connections

### Client Components
- `DictionaryClient`: Main client class handling server communication
- `DictionaryGUI`: User interface for dictionary operations

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 18 or higher
- Java Runtime Environment (JRE)

### Installation

1. Clone the repository
2. Build the server:
```bash
cd DictionaryServer
javac -d out/production/DictionaryServer src/server/.java
jar cvfm DictionaryServer.jar src/META-INF/MANIFEST.MF -C out/production/DictionaryServer .
```

3. Build the client:
```bash
cd DictionaryClient
javac -d out/production/DictionaryClient src/client/*.java
jar cvfm DictionaryClient.jar src/META-INF/MANIFEST.MF -C out/production/DictionaryClient .
```

### Running the Application

1. Start the server:
```bash
java -jar DictionaryServer.jar <port> <dictionary-file-path>
```

2. Start the client:
```bash
java -jar DictionaryClient.jar <server-address> <server-port>
```

## Usage

### Server Control Panel
- Click "Start Server" to begin accepting client connections
- Monitor server status through the GUI
- Click "Stop Server" to terminate the server

### Client Interface
- **Query**: Enter a word and click "Query" to find its meanings
- **Add**: Enter a word and its meanings (semicolon-separated) and click "Add"
- **Remove**: Enter a word and click "Remove" to delete it
- **Update**: Enter a word and new meanings to add to existing definitions

## Protocol Specification

### Request Format
`<action>:<word>:<meanings>`

Actions:
- `query`: Retrieve word meanings
- `add`: Add new word with meanings
- `remove`: Delete a word
- `update`: Add new meanings to existing word

Meanings are semicolon-separated strings.

### Response Format
- Success: `Success: <message>`
- Error: `Error: <message>`

## Technical Details

### Threading Model
- Custom thread pool implementation
- Fixed pool size of 16 threads
- Thread-safe dictionary operations using synchronization

### File Format
Dictionary data is stored in a text file with the format:
```
word:meaning1;meaning2;meaning3
```

## Error Handling
- Connection failures
- Invalid requests
- Duplicate entries
- Missing words/meanings
- File I/O errors

## License
This project is licensed under the MIT License - see the LICENSE file for details.
