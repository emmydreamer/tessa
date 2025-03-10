# Tessa Project Overview

Welcome to the **Tessa Project**

This project aims to create a comprehensive testing framework for **PaperMC servers**.
Initially it will serve as a fabric client side mod and paper plugin, enabling clients 
to execute scripted tasks with a user-friendly API while recording relevant server 
internals to the server console and the chat screen. 

### Motivation

The motivation behind Tessa includes

- To give back to the minecraft community for their contributions.
- To provide a robust **testing framework** for **PaperMC plugins**.

---

### Server types

- **PaperMC**
- **Spigot/Bukkit** : We hope for compatibility but are making design decisions
in the direction of what's best for **PaperMC plugins** on a **PaperMC server**.
- **Fabric modded** : No support planned and not the target.

## Setup Instructions

Tessa was recently started and is in its early development stage.

*** Contributors are welcome *** and can assist with various tasks.

### Core Features (In Progress)

1. Driving the client for the following tasks:
- Moving to a specific location
- Initiating and terminating block breaks
- Handling player join/quit events
- Simulating player quit during an ongoing task

2. Other useful functionality to implement:
- PaperMC plugin to record server data on driven client
- Sending arbitrary packets to paper server
- Automated inputs to craft/enchant/etc
- Incorporating pathfinding algorithms
- Fighting algorithms for testing entity AI. 

---

## Dependencies

- **Java Minecraft** : 1.24
- **Fabric Loader** : 0.16.10
- **Fabric API** : 0.118.0+1.21.4
- **Fabric Loom Plugin** : '1.10-SNAPSHOT'
- **Maven-Publish Plugin** : unversioned but need at least Gradle 6.0
- **Gradle Version** : 8.12.1
- **Minecraft Mojang Mappings** : for Minecraft 1.24

---

## Useful Links

- **Fabric Wiki** : https://wiki.fabricmc.net/start
- **PaperMC** : https://papermc.io/

Rather than trying to match a specific fabric api java doc version, the project recommends doing a git clone, opening up
Intellij, and looking in the external libraries after your gradle project is imported. The entire API is in the libraries.

## Contributions
### Authors
- **emmydreamer**: tessamc1 <[at]> proton <[dot]> me

### Interested in contributing?
Feel free to contribute in any of the above-mentioned areas if you're interested!

