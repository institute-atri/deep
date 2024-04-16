# Endpoints

### Autenticação e Usuários

- **POST /auth/login** - Autenticar usuários no sistema.
- **POST /auth/logout** - Deslogar usuários do sistema.
- **GET /users/me** - Obter informações do perfil do usuário logado.
- **PUT /users/me/update** - Atualizar informações do perfil do usuário.

### Casos Criminais

- **POST /cases** - Criar um novo caso criminal.
- **GET /cases/{id}** - Obter detalhes de um caso específico.
- **GET /cases** - Listar todos os casos, com filtros opcionais por data, tipo de crime, etc.
- **PUT /cases/{id}** - Atualizar um caso específico.
- **DELETE /cases/{id}** - Excluir um caso específico.

### Documentos e Evidências

- **POST /documents** - Carregar documentos relacionados a um caso.
- **GET /documents/{id}** - Visualizar um documento específico.
- **GET /cases/{caseId}/documents** - Listar todos os documentos de um caso.
- **DELETE /documents/{id}** - Excluir um documento específico.

### Análise de Dados

- **POST /analysis/extract-entities** - Extrair entidades e relações de um documento.
- **POST /analysis/sentiment** - Analisar o sentimento de um trecho de texto.
- **GET /analysis/relationships/{caseId}** - Obter um mapa de relacionamentos para um caso.

### Busca e Recomendações

- **GET /search** - Buscar nos casos, documentos e entidades com critérios específicos.
- **GET /recommendations/{caseId}** - Obter recomendações de investigação para um caso.

### Visualizações e Relatórios

- **GET /cases/{caseId}/timeline** - Obter a linha do tempo dos eventos de um caso.
- **GET /cases/{caseId}/report** - Gerar um relatório completo do caso.

### Configurações e Utilidades

- **GET /settings** - Obter as configurações do sistema.
- **PUT /settings** - Atualizar as configurações do sistema.
- **POST /util/upload** - Endpoint genérico para upload de arquivos.

### Monitoramento e Auditoria

- **GET /audit/logs** - Obter registros de auditoria do sistema.
- **GET /audit/actions** - Listar ações auditáveis para revisão e análise.

### Gerenciamento de Usuários (para administradores)

- **POST /admin/users** - Criar um novo usuário.
- **GET /admin/users** - Listar todos os usuários.
- **PUT /admin/users/{userId}** - Atualizar um usuário específico.
- **DELETE /admin/users/{userId}** - Excluir um usuário.