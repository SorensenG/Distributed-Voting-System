<div align="center">

# 🗳️ Distributed Voting System

### Sistema de Votação Eletrônica Distribuída com Arquitetura Cliente-Servidor

*Plataforma segura e escalável para eleições digitais utilizando TCP/IP e processamento concorrente*

<img src="https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17+" />
<img src="https://img.shields.io/badge/TCP/IP-Sockets-0078D7?style=for-the-badge&logo=cisco&logoColor=white" alt="TCP/IP" />
<img src="https://img.shields.io/badge/Multithreading-Active-00D084?style=for-the-badge&logo=java&logoColor=white" alt="Multithreading" />
<img src="https://img.shields.io/badge/GUI-Swing-5382A1?style=for-the-badge&logo=java&logoColor=white" alt="Swing" />
<img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge" alt="MIT" />
</div>

---

## 📋 Sobre

O **Distributed Voting System** é uma aplicação que implementa um sistema completo de votação eletrônica distribuída, permitindo que múltiplos eleitores participem simultaneamente de eleições seguras através de uma rede TCP/IP[web:1][web:2].

**Desenvolvido para:** SI400 – Programação Orientada a Objetos II | 2º Semestre 2025  
**Orientador:** Prof. Dr. André F. de Angelis

### 🎯 Por que este projeto?

- **Concorrência Real**: Gerencia centenas de conexões simultâneas usando threads Java
- **Segurança**: Validação de CPF e prevenção de votos duplicados com estruturas thread-safe
- **Tempo Real**: Dashboard administrativo com atualização instantânea dos resultados
- **Multiplataforma**: Exportação automática em TXT, CSV e JSON

---

## ✨ Características Principais

<table>
<tr>
<td width="50%">

### 🖥️ Servidor

- ✅ **Gerenciamento Concorrente**: Suporte a múltiplos clientes via `ClientHandler` threads
- ✅ **Dashboard Administrativo**: Interface Swing com gráficos em tempo real
- ✅ **Validação Robusta**: Sistema anti-fraude com verificação de CPF
- ✅ **Exportação Múltipla**: Relatórios em TXT, CSV e JSON
- ✅ **Sincronização Thread-Safe**: `ConcurrentHashMap` para dados críticos

</td>
<td width="50%">

### 🗳️ Cliente

- ✅ **Interface Intuitiva**: GUI Swing com navegação por abas
- ✅ **Conexão Estável**: Socket TCP/IP com tratamento de erros
- ✅ **Autenticação Segura**: Validação de CPF antes do voto
- ✅ **Feedback Visual**: Confirmação imediata do voto registrado
- ✅ **Modo Offline**: Sistema de ajuda integrado

</td>
</tr>
</table>

---

## 🚀 Instalação

### Pré-requisitos

☕ Java 17 ou superior
📦 JDK instalado e configurado no PATH
## 🎮 Como Usar

### 1️⃣ Iniciar o Servidor

Via terminal
java -cp bin server.core.VotingServer

Ou execute a classe VotingServer pelo seu IDE
text

O servidor iniciará na **porta 28399** e abrirá o dashboard administrativo automaticamente.

### 2️⃣ Conectar Clientes

Em outro terminal (ou máquina diferente)
java -cp bin cliente.core.VotingClient

Configure o IP do servidor quando solicitado
text

Cada cliente receberá a cédula eleitoral e poderá votar após validação de CPF.

### 3️⃣ Encerrar Eleição

No dashboard do servidor, clique em **"Encerrar Eleição"** para:
- Fechar novas conexões
- Gerar relatórios automáticos
- Exibir estatísticas finais

