## Taura Bots Manager

O projeto Taura Bots Manager é um sistema de gerenciamento de atividades e membros para o Grupo de Pesquisa e Competição Taura Bots, utiliza a linguagem Java com a biblioteca JavaFX para a interface gráfica. Ele oferece, por hora, funcionalidades básicas de CRUD (Create, Read, Update, Delete) para gerenciar membros e atividades do grupo.

## Estrutura de Pastas

O workspace contém duas pastas por padrão, onde:

- `lib`: Contém as bibliotecas externas necessárias para o projeto;
- `src`: Contém os arquivos fonte do projeto;
  - `app`: Contém a classe principal que inicia o aplicativo JavaFX.
  - `controller`: Contém as classes responsáveis pelo controle da lógica do aplicativo e interação com a interface gráfica.
  - `model`: Contém as classes que representam os dados do sistema, como Membro e Atividade.
  - `resources`: Contém os recursos estáticos, como arquivos de configuração e imagens.
  - `view`: Contém os arquivos de visualização em tabela dos elementos das coleções.
  

## Requisitos do Sistema

- Java Development Kit (JDK) 11 ou superior
- JavaFX SDK 11 ou superior
- IDE recomendada: IntelliJ IDEA, Eclipse ou VSCode com suporte a Java

## Futuras Implementações

- Implementação de um banco de dados para armazenamento persistente dos dados;
- Adição de funcionalidades avançadas de busca e filtragem;
- Melhoria da interface gráfica com mais opções de personalização;
- Implementação de autenticação e controle de acesso para diferentes níveis de usuários;
- Integração com serviços externos, como calendários e planilhas online.
- Modularização do código para facilitar a manutenção e expansão futura.
- Implementação do Taura Bar, um sistema de gerenciamento do bar e cafeteria do grupo.
