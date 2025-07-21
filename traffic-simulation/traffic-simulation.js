import http from 'k6/http';
import { check, fail, sleep } from 'k6';
import { randomItem, uuidv4 } from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';

export const options = {
    stages: [
        { duration: '1m', target: 5 }, // Monte à 120 utilisateurs (2 commandes/sec)
        { duration: '8m', target: 5 }, // Maintien du trafic
        { duration: '1m', target: 0 }, // Descente progressive
    ],
};

const PRODUCT_API = 'https://api.devoxx-demo.sbx.aws.ippon.fr/product-service/api/products';
const CART_API = 'https://api.devoxx-demo.sbx.aws.ippon.fr/shopping-cart-service/api/shopping-carts';
const ORDER_API = 'https://api.devoxx-demo.sbx.aws.ippon.fr/order-service/api/orders';
const STOCK_API = 'https://api.devoxx-demo.sbx.aws.ippon.fr/stock-service/api/stocks';

export default function () {
    // 1. Récupération des produits
    const productsResponse = http.get(PRODUCT_API);
    check(productsResponse, {
        'GET /api/products status is 200': (res) => res.status === 200,
    });

    const products = productsResponse.json();
    if (!products.length) {
        console.error('No products found');
        return;
    }

    // 2. Création d'un panier
    const shoppingCartId = uuidv4();

    // 3. Ajout de produits aléatoires dans le panier
    const selectedProducts = [];
    for (let i = 0; i < Math.floor(Math.random() * 3) + 1; i++) {
        const product = randomItem(products);
        const quantity = Math.floor(Math.random() * 3) + 1
        selectedProducts.push({ id: product.id, quantity: quantity });

        // On s'assure qu'il y ait assez de stock.
        const stockToAddPayload = JSON.stringify({ quantity: quantity });
        let res = http.post(`${STOCK_API}?productId=${product.id}`, stockToAddPayload, {
            headers: { 'Content-Type': 'application/json' },
        });

        const addToCartResponse = http.put(`${CART_API}/${shoppingCartId}/products`, JSON.stringify(selectedProducts[i]), {
            headers: { 'Content-Type': 'application/json' },
        });

        if (
            !check(addToCartResponse, {
            'PUT /api/shopping-carts/{id}/products status is 201': (res) => res.status === 201,
            })
        ){
            fail('Not enough stock for ' + product.name);
        }
    }

    // 4. Passer la commande
    const orderPayload = JSON.stringify({ cartId: shoppingCartId });
    const orderResponse = http.put(ORDER_API, orderPayload, {
        headers: { 'Content-Type': 'application/json' },
    });

    check(orderResponse, {
        'PUT /api/orders status is 200': (res) => res.status === 204,
    });

    // Pause aléatoire pour éviter un trafic parfaitement régulier
    sleep(Math.random() * 0.5);
}
