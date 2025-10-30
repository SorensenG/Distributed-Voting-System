<h1 align="center">🗳️ Distributed Voting System</h1>

<p align="center">
  <strong>Sistema de votação eletrônica distribuída usando Java TCP/IP</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk" alt="Java 17+" />
  <img src="https://img.shields.io/badge/Paradigm-Client--Server-blue?style=for-the-badge&logo=socketdotio" alt="Client-Server" />
  <img src="https://img.shields.io/badge/Multithreading-Enabled-success?style=for-the-badge&logo=java" alt="Multithreading" />
  <img src="https://img.shields.io/badge/License-MIT-lightgrey?style=for-the-badge" alt="MIT License" />
</p>

---

## 🔍 Sobre o Projeto

O **Distributed Voting System** é uma aplicação cliente-servidor desenvolvida em Java que simula um processo de votação eletrônica seguro e distribuído utilizando comunicação **TCP/IP**, **object streams** e **multithreading**.

Este projeto foi desenvolvido para o módulo de *Sistemas Distribuídos* com o objetivo de aplicar conceitos práticos de redes, programação concorrente e design de interfaces gráficas com **Java Swing**.

### 🎯 Objetivos

- Implementar um sistema de votação distribuído e escalável
- Garantir a integridade dos votos através de validação de CPF
- Permitir votação simultânea de múltiplos clientes
- Exibir resultados em tempo real para o administrador

---

## ✨ Funcionalidades

### 🖥️ Módulo Servidor

- ✅ Aceita múltiplas conexões simultâneas de clientes (multithreading)
- ✅ Valida CPF e previne votos duplicados
- ✅ Exibe resultados da votação em tempo real através de dashboard Swing
- ✅ Gera relatório final (`results.txt`) ao término da eleição
- ✅ Controle administrativo para iniciar/encerrar votações

### 🗳️ Módulo Cliente

- ✅ Conecta ao servidor via TCP/IP
- ✅ Recebe a pergunta da eleição e opções disponíveis
- ✅ Autenticação via CPF
- ✅ Permite envio de um único voto por eleitor
- ✅ Interface amigável construída com **Swing**

---

## 🏗️ Arquitetura

O sistema utiliza uma arquitetura cliente-servidor com os seguintes componentes:

| Camada | Pacote | Descrição |
|--------|---------|-----------|
| **Common** | `common.*` | Classes serializáveis compartilhadas e tipos de mensagens |
| **Server** | `server.core` | Lógica multithreaded do servidor e validação de votos |
| **Client** | `client.core` | Comunicação cliente-servidor e submissão de votos |
| **GUI** | `server.gui` / `client.gui` | Interfaces Java Swing para ambos os módulos |

### 🔄 Fluxo de Comunicação

1. **Cliente** se conecta ao servidor via Socket TCP/IP
2. **Servidor** cria uma thread dedicada para cada cliente
3. **Cliente** recebe dados da eleição via ObjectInputStream
4. **Cliente** envia voto autenticado via ObjectOutputStream
5. **Servidor** valida CPF, registra voto e atualiza dashboard
6. **Servidor** envia confirmação ao cliente

---

## 🛠 Tecnologias Utilizadas

- **Java 17+** - Linguagem de programação
- **Java Swing** - Interface gráfica
- **TCP/IP Sockets** - Comunicação de rede
- **ObjectInputStream/OutputStream** - Serialização de objetos
- **Multithreading** - Processamento concorrente
- **Maven/Gradle** *(opcional)* - Gerenciamento de dependências

---

## 📋 Pré-requisitos

Antes de começar, você precisará ter instalado:

- [Java JDK 17+](https://www.oracle.com/java/technologies/downloads/)
- [Git](https://git-scm.com)
- IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code)

---
## 👤 Autor

**Gabriel Sorense**
- GitHub: [SorensenG](https://github.com/SorensenG)
- LinkedIn: [Gabriel Sorensen M Traina](https://www.linkedin.com/in/gabriel-sorensen/)
- Email: g.soren.sen2004@gmail.com

---

