# 🛍️ PRODUCTOS API Endpoints

This service exposes RESTful endpoints for managing products and their inventory.  
It follows a layered architecture (`integration`, `business`, `presentation`).

## 🌐 Base URL

http://localhost:8080/api/v1

---

## · Products

| Method | Endpoint               | Descripción                             |
|--------|------------------------|-----------------------------------------|
| GET    | `/products`            | Get all products                        |
| POST   | `/products`            | Create new product                      |
| GET    | `/products/{id}`       | Get a product by its ID                 |
| PUT    | `/products/{id}`       | Update a product by its ID              |
| DELETE | `/products/{id}`       | Delete a product by its ID              |
| PATCH  | `/products/{id}`       | Update the stock of a product by its ID |

---

## · Promotions

| Method | Endpoint                   | Descripción                             |
|--------|----------------------------|-----------------------------------------|
| GET    | `/promotionsQuantity`      | Get all quantity-based promotions       |
| POST   | `/promotionsQuantity`      | Create a quantity-based promotion       |
| GET    | `/promotionsQuantity/{id}` | Get a quantity-based promotion by its i |
| POST   | `/promotionsQuantity`      | Create a quantity-based promotion       |
| POST   | `/promotionsQuantity`      | Create a quantity-based promotion       |

---

## · InventoryData (inside Product)

Each product has the following inventory fields:

| Campo        | Tipo   | Descripción                    |
|--------------|--------|--------------------------------|
| `stock`      | int32  | Available units                |
| `threshold`  | int32  | Limit to trigger restock event |
| `totalSales` | int32  | Total units sold               |

---

## · Enum: Product Category

The possible values for the `category` field are:

- `TOYS`
- `BOOKS`
- `SPORTS`
- `MEAL`
- `CLOTHES`

---

## · Enum: Promotion Type

The possible values for the `promotionType` field are:

- `SEASON`
- `QUANTITY`

---

## 🗄️ Database diagram

![DB Diagram](./diagrams/db.svg)

---

## 📦 Classes Diagram

![CL Diagram](./diagrams/models.svg)
