# e.sun-bank-practice

本專案為玉山銀行練習題實作，透過 **Vue.js** 與 **Spring Boot** 建立一個全端 CRUD 系統，結合 RESTful API 與 MySQL 資料庫，實現使用者註冊、產品管理與喜好清單功能。

---

## 📌 專案架構

- **前端**：Vue 3 + Composition API + Pinia  
- **後端**：Spring Boot + RESTful API  
- **資料庫**：MySQL（搭配 Stored Procedure 與 Transaction）

架構流程如下：

```
[Vue.js 前端]
     ⇅ RESTful API
[Spring Boot 後端應用層]
     ⇅ JDBC + Stored Procedure
[MySQL 資料庫]
```

### ✅ 系統特色

- 分層架構設計（Controller / Service / DAO）
- 使用 Stored Procedure 處理資料邏輯，前後端分離
- Transaction 控制，確保資料一致性
- 防止 SQL Injection 與 XSS 攻擊
- 清晰的操作界面與功能模組

---

## 📁 專案結構

```bash
e.sun-bank-practice/
│
├── frontend/             # Vue 前端專案
│   └── src/
│       └── components/   # 各個功能元件
│
├── backend/              # Spring Boot 後端專案
│   ├── controller/       # API 控制層
│   ├── service/          # 業務邏輯層
│   ├── repository/       # DAO 資料存取層
│   └── db/
│       ├── ddl.sql       # 資料表建立語法
│       └── dml.sql       # 初始資料與 Stored Procedures
│
└── README.md             # 本說明文件
```

---

## 🚀 快速開始

### 1. 專案下載

```bash
git clone https://github.com/weitingyu1127/e.sun-bank-practice.git
cd e.sun-bank-practice
```

### 2. 資料庫設定

請確保你已啟動 MySQL 並建立如下資料庫：

```sql
CREATE DATABASE `e.sun`;
```

匯入 SQL：

```bash
backend/DB/DDL.sql      
backend/DB/DML.sql      
```

資料庫連線設定（預設）：

```
jdbc:mysql://localhost:3307/e.sun?useSSL=false&serverTimezone=Asia/Taipei
```

請將 MySQL 監聽在 `3307`，或修改 `application.properties` 為你的埠號。

### 3. 啟動後端

```bash
cd backend
./mvnw spring-boot:run
```

後端服務位於：

```
http://localhost:8080
```

### 4. 啟動前端

```bash
cd frontend
npm install
npm run dev
```

前端開發伺服器預設網址為：

```
http://localhost:5173
```

---

## 🧩 操作說明

- **登入 / 註冊**：使用者可建立自己的帳號並登入系統。
- **個人資訊 Table**：登入後可新增多個付款帳號。
- **產品列表 Table**：使用者可新增商品（名稱、價格、手續費），供他人參考。
- **喜好清單 Table**：可將商品加入清單，並進行編輯與刪除。
- **查詢功能**：支援快速搜尋喜好商品。

---

## 🛡️ 安全性設計

- ✅ 使用 PreparedStatement 防止 SQL Injection  
- ✅ Vue 輸入欄位經過 HTML Escape 防範 XSS  
- ✅ RESTful API 實作權限控管與資料驗證  
- ✅ 所有異動操作皆包覆於 Transaction 中

---

## 🌐 前後端介接 API 範例

```http
GET  /api/products              // 取得所有商品
POST /api/likelist/add          // 加入喜好清單
POST /api/likelist/update       // 更新喜好清單項目
DELETE /api/likelist/delete     // 刪除喜好商品
POST /api/user/login            // 使用者登入
POST /api/user/register         // 註冊新使用者
```

---

## 📷 畫面預覽

> 你可以放置以下範例圖片路徑：
> 
> ![登入頁面](./assets/Login.png)
> ![註冊頁面](./assets/Signup.png)
> ![首頁](./assets/HonePage.png)
> ![加入產品](./assets/AddProduct.png)
> ![加入喜好](./assets/AddFavorite.png)
> ![編輯喜好](./assets/EditFavorite.png)
---