CREATE TABLE products(
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1024) NOT NULL,
    price INTEGER NOT NULL,
    category_id UUID REFERENCES categories(id),
    quantity_stored INTEGER NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);