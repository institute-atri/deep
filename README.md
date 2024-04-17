# 🧜‍♀️ DEEP - Digital Evidence Evaluation Profiler 🧜‍♀️

DEEP aims to improve the ability of law enforcement and legal professionals to solve crimes by analyzing large volumes
of data related to criminal cases. Using natural language processing and learning techniques
machine, the software can extract relevant information, identify hidden patterns and suggest connections that can
not be immediately apparent to human investigators.

- Digital: Reflects the focus on technology and digital data, which are crucial in modern forensic analyses.
- Evidence: Emphasizes the system's primary goal, which is to handle evidence in criminal cases.
- Evaluation: Indicates that the system not only collects but also evaluates the evidence, a crucial step in forensic
  analysis.
- Profiler: Suggests that the system goes beyond mere evidence evaluation to also provide a more in-depth profile or
  analysis, which can be especially useful in complex investigations.

# 🧜‍♀️ Features

## Padronização

- Documentação OpenAPI - Swagger
- Manter o nível de maturidade de Richardson na API Restful
- TDD -> JUnit 5, Mockito, Cucumber

- [ ] **Chat with IA** - Nicholas, Kevin
    - [ ] **POST /chat**: Send a message to the IA and receive a response.
    - [ ] **GET /chat/{id}**: Get the chat history for a specific case.
    - [ ] **GET /chat**: List all chat messages, with optional filters by date, user, etc.
    - [ ] **PUT /chat/{id}**: Update a specific chat message.
    - [ ] **DELETE /chat/{id}**: Delete a specific chat message.

- [ ] **Cases** - Nicolly
    - [ ] **POST /case**: Create a new criminal case.
    - [ ] **GET /cases/{id}**: Get details of a specific case.
    - [ ] **GET /cases**: List all cases, with optional filters by date, crime type, etc.
    - [ ] **PUT /cases/{id}**: Update a specific case.
    - [ ] **DELETE /cases/{id}**: Delete a specific case.


- [ ] **Documents and Evidence** - Wendel
    - [ ] **POST /documents**: Upload documents related to a case.
    - [ ] **GET /documents/{id}**: View a specific document.
    - [ ] **GET /cases/{caseId}/documents**: List all documents for a case.
    - [ ] **PUT /documents/{id}**: Update a specific document.
    - [ ] **DELETE /documents/{id}**: Delete a specific document.


- [ ] **Authentication and Users** - Rafael
    - [ ] **POST /auth/login**: Authenticate users in the system.
    - [ ] **POST /auth/logout**: Log users out of the system.
    - [ ] **GET /users/me**: Get information about the logged-in user's profile.
    - [ ] **PUT /users/me/update**: Update user profile information.

## 🧜‍♀️ General Features

- [ ] **User Authentication**: Secure login and logout functionality for users.
- [ ] **User Management**: Create, update, and delete user accounts.
- [ ] **Case Management**: Create, update, and delete criminal cases.
- [ ] **Document Management**: Upload, view, and delete documents related to cases.
- [ ] **Data Analysis**: Extract entities and relationships from documents, analyze sentiment, and generate relationship
  maps.
- [ ] **Search and Recommendations**: Search cases, documents, and entities with specific criteria, and get
  investigation recommendations.
- [ ] **Visualizations and Reports**: Generate timelines and reports for cases.
- [ ] **Settings and Utilities**: Manage system settings and upload files.
- [ ] **Monitoring and Auditing**: View audit logs and actions for system review.

# 🧜‍♀️ License

```
MIT License

Copyright (c) 2024 ATRI - Advanced Technology Research Institute

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

