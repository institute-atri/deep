# Motivação

### Objetivo do DEEP

O DEEP visa aprimorar a capacidade das autoridades e profissionais legais de resolver crimes ao analisar grandes volumes
de dados relacionados a casos criminais. Utilizando técnicas de processamento de linguagem natural e aprendizado de
máquina, o software consegue extrair informações relevantes, identificar padrões ocultos e sugerir conexões que podem
não ser imediatamente aparentes para investigadores humanos.

### Por Que Ele Deve Ser Criado

O volume de dados gerados em investigações criminais é vasto e continua crescendo. A capacidade de processar rapidamente
essa informação e extrair insights valiosos pode ser a chave para resolver casos mais rapidamente, desonerar os
inocentes e garantir que os culpados sejam trazidos à justiça. O DEEP tem o potencial de transformar esse processo,
tornando as investigações mais eficientes e baseadas em evidências.

### Onde Vai Ser Utilizado

O DEEP é projetado para ser utilizado por departamentos de polícia, agências governamentais de aplicação da lei,
escritórios de promotores e advogados defensores. Essas entidades podem usar o software tanto em suas operações diárias
para casos correntes quanto em esforços para resolver casos frios ou revisar condenações anteriores à luz de novas
evidências, ou tecnologias analíticas.

### Por Quem Ele Vai Ser Utilizado

Investigadores criminais, analistas de dados forenses, promotores e advogados defensores são os principais usuários do
DEEP. Esses profissionais podem utilizar o software para diversas finalidades, desde a preparação inicial de um caso até
as etapas finais de um julgamento, oferecendo suporte analítico em cada fase do processo legal e investigativo.

### Como Ele Vai Ser Utilizado

Os usuários inserirão dados do caso, como boletins de ocorrência, transcrições de interrogatórios, evidências digitais,
e notas de investigação no DEEP. O software então processará essas informações, aplicará seus algoritmos de IA para
analisar os dados, e fornecerá insights, como conexões entre casos, sugestões de pessoas ou locais a investigar, e
análises de probabilidade de cenários. Esses resultados ajudarão a guiar a estratégia dos investigadores e a tomada de
decisão.

### Como Ele Vai Ajudar a Sociedade

Ao melhorar a eficiência e eficácia das investigações criminais, o DEEP tem o potencial de contribuir significativamente
para a sociedade de várias maneiras:

- **Justiça mais rápida e precisa:** Reduzindo o tempo necessário para resolver crimes e aumentando a precisão das
  investigações, contribui para um sistema de justiça mais eficaz e confiável.
- **Desoneração dos inocentes:** Ao analisar de forma mais abrangente as evidências disponíveis, o DEEP pode ajudar a
  evitar condenações injustas.
- **Recursos otimizados:** Libera recursos humanos e financeiros ao aumentar a eficiência das investigações, permitindo
  que as autoridades concentrem seus esforços em casos que exigem atenção humana direta.
- **Segurança pública melhorada:** Resolver crimes de forma mais rápida e eficaz pode dissuadir atividades criminosas
  futuras, contribuindo para comunidades mais seguras.

Em suma, o DEEP representa uma evolução promissora na intersecção da tecnologia e da justiça criminal, prometendo tornar
o processo de resolução de crimes mais ágil, justo e baseado em evidências.

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