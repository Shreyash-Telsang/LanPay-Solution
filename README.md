# LANPay - Offline Payment Solution<!-- Add if you have a logo -->

A blockchain-based offline payment system enabling secure transactions without internet connectivity, designed for rural and remote areas.

## Table of Contents
- [Problem Statement](#problem-statement)
- [Solution](#solution)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [System Architecture](#system-architecture)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Problem Statement
In areas with limited or no internet connectivity, digital payments are often inaccessible, leaving residents and businesses unable to benefit from cashless transactions. This system enables secure transactions without requiring continuous internet access.

## Solution
LANPay provides:
- Offline blockchain wallet for secure transactions
- Mesh network infrastructure for local connectivity
- Synchronization with main blockchain when online
- QR/NFC based authentication
- SPV nodes for lightweight validation

## Features
- **Offline Transactions**: Conduct payments without internet
- **Local Validation**: Transactions verified within local network
- **Secure**: Blockchain-based encryption and authentication
- **Scalable**: Hybrid blockchain approach
- **User-Friendly**: Simple setup and transaction flow

## Technology Stack
### Backend
- Python (Flask)
- Web3.py
- Ethereum Blockchain
- Smart Contracts (Solidity)

### Database
- MySQL

### Security
- AES-256 Encryption
- Cryptographic Hashing
- Merkle Trees

### Infrastructure
- Mesh Network Topology
- SPV Nodes
- Full Nodes for validation

## System Architecture
![System Architecture](assets/architecture.png) <!-- Add diagram if available -->

1. **User Registration**: Users create accounts and load virtual currency
2. **Offline Transaction**: Users initiate transactions via QR codes
3. **Local Validation**: Transactions verified by nearby SPV nodes
4. **Blockchain Sync**: Transactions synced to main blockchain when online
5. **Fund Redemption**: Merchants convert virtual currency to cash when online

## Installation

### Prerequisites
- Python 3.8+
- Node.js (for local blockchain)
- MySQL
- Git

### Setup
```bash
# Clone the repository
git clone https://github.com/Shreyash-Telsang/LanPay-Solution.git
cd LanPay-Solution

# Set up virtual environment
python -m venv venv
source venv/bin/activate  # On Windows use `venv\Scripts\activate`

# Install dependencies
pip install -r requirements.txt

# Set up local blockchain (using Hardhat)
cd my-blockchain
npm install
npx hardhat node

# Set up database
mysql -u root -p < database/schema.sql
