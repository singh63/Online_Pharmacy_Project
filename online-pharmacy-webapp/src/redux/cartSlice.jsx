import {createSlice} from '@reduxjs/toolkit';
import axios from "axios";

const getCartId = () => JSON.parse(localStorage.getItem("cartId"));
const getToken = () => JSON.parse(localStorage.getItem("token"));

const updateCart = (productId, quantity) => {
    axios.post(`http://localhost:8080/api/users/carts/${getCartId()}/products/${productId}/quantity/${quantity}`, null, {
        headers: {
            Authorization: `Bearer ${getToken()}`
        }
    });
};

const initialState = [];

export const cartSlice = createSlice({
    name: 'cart',
    initialState,
    reducers: {
        initializeCart: (state, action) => {
            state.length = 0;
            const products = action.payload;
            if (products.length > 0) {
                products.map(item => state.push(item));
            }
        },
        addToCart(state, action) {
            // Check if the item already exists in the cart
            const existingItem = state.find(item => item.productId === action.payload.productId);
            if (existingItem) {
                // Check available quantity before adding
                if (existingItem.quantity + action.payload.quantity <= action.payload.availableQuantity) {
                    existingItem.quantity += action.payload.quantity;
                    updateCart(action.payload.productId, existingItem.quantity);
                } else {
                    console.error('Cannot exceed available quantity');
                }
            } else {
                state.push(action.payload);
                updateCart(action.payload.productId, action.payload.quantity);
            }
        },
        deleteFromCart(state, action) {
            updateCart(action.payload.productId, 0);
            return state.filter(item => item.productId !== action.payload.productId);
        },
        incrementQuantity(state, action) {
            const item = state.find(item => item.productId === action.payload.productId);
            if (item && item.quantity < action.payload.quantity) {
                item.quantity++;  // Increment only if it doesn't exceed availableQuantity
                updateCart(action.payload.productId, item.quantity);
            } else {
                console.error('Cannot increment beyond available quantity');
            }
        },
        decrementQuantity(state, action) {
            const item = state.find(item => item.productId === action.payload);
            if (item && item.quantity > 1) {
                item.quantity--;  // Decrement only if quantity is greater than 1
                updateCart(action.payload, item.quantity);
            } else {
                console.error('Cannot decrement below 1');
            }
        },
        resetCart() {
            return [];  // Reset the cart to an empty state
        },
    },
});

// Action creators are generated for each case reducer function
export const {
    initializeCart,
    addToCart,
    deleteFromCart,
    incrementQuantity,
    decrementQuantity,
    resetCart
} = cartSlice.actions;

export default cartSlice.reducer;
