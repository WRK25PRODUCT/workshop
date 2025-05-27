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

## · InventoryData (inside Product)

Each product has the following fields of Inventory:

| Campo       | Tipo    | Descripción                    |
|-------------|---------|--------------------------------|
| `stock`     | int32   | Available units                |
| `threshold` | int32   | Limit to trigger the `restock` |
| `totalSales`| int32   | Total units sold               |

---

## · Enum: Product Category

The possible values for the `category` field are:

- `TOYS`
- `BOOKS`
- `SPORTS`
- `MEAL`
- `CLOTHES`

---



## 🗄️ Database diagram

![DB Diagram](./diagrams/db.svg)

---

## 📦 Classes Diagram

![CL Diagram](./diagrams/models.svg)

