CREATE TABLE orders(
    id UUID PRIMARY KEY ,
    purchase_date TIMESTAMPTZ NOT NULL,
    status VARCHAR(15) NOT NULL CHECK(status in ('PENDING', 'PAYED', 'CANCELED')),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);