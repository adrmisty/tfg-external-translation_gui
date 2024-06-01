# tfg-external-translation_gui

## 💡 Description
Repository for my **TFG (Final Degree Project)**: _externalized translation service for programs_.

Developed by **Adriana Rodríguez Flórez**, undergraduate student at the BSc in Computer Science
and Software Engineering at the University of Oviedo (Asturias, Spain).


## 🏛️ Architecture

### Database
* SQLite.

### Frameworks and APIs
(All needed libraries and dependencies are specified in the pom.xml file!)

* GUI: Swing
* Model access: OpenAI ChatCompletionsAPI (Java SDK service, provided by https://github.com/TheoKanning/openai-java).
* Text to speech: Microsoft Azure AI Speech (https://azure.microsoft.com/en-us/products/ai-services/ai-speech).
* Image description: Microsoft Azure AI Vision (https://azure.microsoft.com/en-us/products/ai-services/ai-vision).
* Database management: JDBC (Java Database Connectivity) for SQLite.