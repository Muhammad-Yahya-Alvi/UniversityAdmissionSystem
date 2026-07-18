# 🎓 University Admission Management System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
![Swing](https://img.shields.io/badge/UI-Swing-blue?style=for-the-badge)

A robust, professional desktop application built with Java Swing and MySQL to streamline the university admission process. Featuring a modern UI/UX, secure authentication, and a centralized management dashboard.

---

## ✨ Key Features

- 🖥️ **Modern UI/UX**: Custom-built ModernButton components with hover animations and smooth transitions.
- 🔐 **Secure Authentication**: Multi-role login system (Admin & Student) with encrypted password handling (simulated).
- 📝 **Admission Portal**: Intuitive forms for students to submit applications and track status.
- 📊 **Admin Dashboard**: Comprehensive overview for administrators to manage student records and admissions.
- ⚙️ **External Configuration**: Database settings managed via .properties files for easy deployment.
- 🗄️ **Relational Database**: Optimized MySQL schema for data integrity.

---

## 🛠️ Tech Stack

- **Language:** Java (JDK 17+)
- **GUI Framework:** Java Swing & AWT
- **Database:** MySQL 8.0
- **Driver:** MySQL Connector/J 8.4.0

---

## 🚀 Getting Started

### 1. Prerequisites
- [Java JDK 17+](https://www.oracle.com/java/technologies/downloads/)
- [MySQL Server](https://dev.mysql.com/downloads/installer/)

### 2. Database Setup
1. Open your MySQL client (Workbench or CMD).
2. Execute the setup.sql script provided in the root directory:
   `sql
   source setup.sql;
   `

### 3. Configuration
Edit the config.properties file in the root directory to match your database credentials:
`properties
db.url=jdbc:mysql://127.0.0.1:3306/university_admission
db.user=your_username
db.password=your_password
`

### 4. Running the Application
Simply double-click the 
un.bat file OR run the following commands:
`ash
javac -cp .;mysql-connector-j-8.4.0.jar *.java
java -cp .;mysql-connector-j-8.4.0.jar LoginFrame
`

---

## 📁 Project Structure
`	ext
UniversityAdmissionSystem/
├── src/ (Future migration)
├── mysql-connector-j-8.4.0.jar  # Database Driver
├── config.properties           # External Configuration
├── setup.sql                   # Database Schema
├── AppTheme.java               # Global Styling
├── ModernButton.java           # Custom UI Components
└── run.bat                     # Automation Script
`

---

## 🤝 Contributing
Contributions are welcome! If you have a suggestion that would make this better, please fork the repo and create a pull request.

---

## 👤 Author
**M. Yahya Alvi**
- GitHub: [Muhammad-Yahya-Alvi](https://github.com/Muhammad-Yahya-Alvi)

---
*Developed as part of University Semester Project.*
