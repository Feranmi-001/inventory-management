# Inventory Management System

This is a multi-tenant SaaS inventory management system built for Nigerian SMEs,
enabling multiple businesses to manage their inventory, sales, suppliers,
and staff from a single platform.

## Features

- Multi-tenant architecture — each business has isolated data
- JWT authentication with role-based access control (OWNER, MANAGER, STAFF)
- Product management with SKU and barcode scanner support
- Sales recording with automatic stock deduction
- Low stock alerts
- Supplier management and stock receiving
- Category management
- Staff management with role-based permissions
- Reports (daily, weekly, monthly, yearly)
- Dashboard with chart-ready data endpoints
- Security hardening (rate limiting, input sanitization, token blacklisting)
- Pagination support

## Tech Stack

- Java 21
- Spring Boot 3.3.5
- MongoDB Atlas
- Spring Security + JWT
- Maven
- Docker

## API Endpoints

### Auth
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | /api/auth/register | Register a new business | Public |
| POST | /api/auth/login | Login | Public |
| POST | /api/auth/logout | Logout | All |

### Products
| Method | Endpoint                  | Description | Access |
|---|---------------------------|---|---|
| POST | /api/products             | Create product | OWNER, MANAGER |
| GET | /api/products             | Get all products | OWNER, MANAGER |
| GET | /api/products/{id}        | Get product by ID | OWNER, MANAGER |
| GET | /api/products/sku/{sku}   | Get product by SKU | OWNER, MANAGER |
| GET | /api/products/search?name= | Search products | OWNER, MANAGER |
| GET | /api/products/low-stock   | Get low stock products | OWNER, MANAGER |
| GET | /api/products/paginated   | Get paginated products | OWNER, MANAGER |
| PUT | /api/products/{id}        | Update product | OWNER, MANAGER |
| DELETE | /api/products/{id}        | Delete product | OWNER, MANAGER |

### Sales
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | /api/sales | Record a sale | ALL |
| GET | /api/sales | Get all sales | OWNER, MANAGER |
| GET | /api/sales/{id} | Get sale by ID | OWNER, MANAGER |
| GET | /api/sales/date-range | Get sales by date range | OWNER, MANAGER |
| GET | /api/sales/payment/{method} | Get sales by payment method | OWNER, MANAGER |
| GET | /api/sales/customer?name= | Get sales by customer | OWNER, MANAGER |
| GET | /api/sales/count | Get total sales count | OWNER, MANAGER |

### Suppliers
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | /api/suppliers | Add supplier | OWNER, MANAGER |
| GET | /api/suppliers | Get all suppliers | OWNER, MANAGER |
| GET | /api/suppliers/{id} | Get supplier by ID | OWNER, MANAGER |
| GET | /api/suppliers/active | Get active suppliers | OWNER, MANAGER |
| GET | /api/suppliers/search?name= | Search suppliers | OWNER, MANAGER |
| PUT | /api/suppliers/{id} | Update supplier | OWNER, MANAGER |
| PUT | /api/suppliers/{id}/deactivate | Deactivate supplier | OWNER, MANAGER |
| POST | /api/suppliers/receive-stock | Receive stock | OWNER, MANAGER |
| GET | /api/suppliers/receive-stock | Get all stock receivings | OWNER, MANAGER |

### Staff
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | /api/staff | Add staff member | OWNER |
| GET | /api/staff | Get all staff | OWNER |
| GET | /api/staff/{id} | Get staff by ID | OWNER |
| PUT | /api/staff/{id}/role | Update staff role | OWNER |
| PUT | /api/staff/{id}/deactivate | Deactivate staff | OWNER |
| PUT | /api/staff/{id}/reactivate | Reactivate staff | OWNER |
| DELETE | /api/staff/{id} | Delete staff | OWNER |

### Categories
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | /api/categories | Create category | OWNER, MANAGER |
| GET | /api/categories | Get all categories | OWNER, MANAGER |
| GET | /api/categories/active | Get active categories | OWNER, MANAGER |
| PUT | /api/categories/{id} | Update category | OWNER, MANAGER |
| PUT | /api/categories/{id}/deactivate | Deactivate category | OWNER, MANAGER |
| DELETE | /api/categories/{id} | Delete category | OWNER, MANAGER |

### Reports
| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | /api/reports/daily | Daily sales report | OWNER, MANAGER |
| GET | /api/reports/weekly | Weekly sales report | OWNER, MANAGER |
| GET | /api/reports/monthly | Monthly sales report | OWNER, MANAGER |
| GET | /api/reports/yearly | Yearly sales report | OWNER, MANAGER |
| GET | /api/reports/best-sellers | Best selling products | OWNER, MANAGER |
| GET | /api/reports/low-stock | Low stock report | OWNER, MANAGER |
| GET | /api/reports/inventory-value | Inventory value report | OWNER, MANAGER |

### Dashboard
| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | /api/dashboard/summary | Dashboard summary | OWNER, MANAGER |
| GET | /api/dashboard/sales-trend | Sales trend (line graph) | OWNER, MANAGER |
| GET | /api/dashboard/payment-breakdown | Payment breakdown (pie chart) | OWNER, MANAGER |
| GET | /api/dashboard/monthly-trend | Monthly trend (bar chart) | OWNER, MANAGER |

## Setup

### Prerequisites
- Java 21
- Maven
- MongoDB Atlas account

### Environment Variables
Create a `.env` file in the root directory:
``` env
MONGODB_URI=your_mongodb_uri
MONGODB_DATABASE=inventory_db
JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=86400000
```
### Run Locally
```bash
mvn spring-boot:run
```

## Security Features
- JWT authentication with token blacklisting
- BCrypt password encryption
- Rate limiting (5 requests/minute on auth endpoints)
- Input sanitization with Jsoup
- Role-based access control
- Multi-tenant data isolation
- Request size limiting

## Future Features
- Email notifications for low stock alerts
- WhatsApp integration
- Debt tracking module
- Multi-branch support
- Offline mode
- AI-powered sales forecasting

## Author
Oluwaferanmi — Backend Software Engineer