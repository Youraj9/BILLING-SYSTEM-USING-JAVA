# 🛒 Supermarket Billing System

A desktop-based application developed using **Java Swing** and **MySQL** to manage billing, inventory, and user authentication for a supermarket. It includes features for both admin and salesperson users, designed with a user-friendly interface and modular backend.

---

## 🚀 Features

### 👨‍💼 Admin Functionality

- Secure Admin Login
- View **Daily Transactions**:
  - Salesperson name
  - Total amount paid
  - CGST and SGST
  - Order date
- View **Inventory Page**:
  - List of all items
  - Quantity and Price
  - **Stock Status**:
    - `SUFFICIENT` (quantity > 20)
    - `NEED STOCK URGENTLY` (quantity < 5; red highlight)
    - `MODERATE` (else)
- Easy navigation between dashboard and inventory

### 👨‍🔧 User (Salesperson) Functionality

- Secure login for users
- Ability to generate bills for purchased items
- Automatically calculates CGST and SGST
- Generates transaction receipts
- Stores each transaction in the database

### 📝 Registration Page

- Allows new users to register with:
  - Username
  - Role
  - Password
- Prevents duplicate usernames with validation

---

## 🧱 Technologies Used

- **Frontend**: Java Swing (GUI)
- **Backend**: MySQL
- **Database Tables**:
  - `users`: Manages login and roles
  - `items`: Manages product inventory
  - `orders`: Stores order records
