db = db.getSiblingDB('mongo-db'); // like 'use mongo-db'
db.createCollection('customer', {autoIndexId: true});
db.customer.createIndex({'email': 1}, {unique: true});
