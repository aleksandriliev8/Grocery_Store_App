import React, { useState } from "react";
import axios from "axios";

const API_BASE_URL = "https://localhost:8443";

export default function AddOrder({ onOrder }) {
  const [products, setProducts] = useState([{ name: "", quantity: "" }]);
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");

  const handleChange = (idx, e) => {
    const newProducts = [...products];
    newProducts[idx][e.target.name] = e.target.value;
    setProducts(newProducts);
  };

  const handleAddProduct = () => {
    setProducts([...products, { name: "", quantity: "" }]);
  };

  const handleRemoveProduct = (idx) => {
    const newProducts = [...products];
    newProducts.splice(idx, 1);
    setProducts(newProducts);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setResult(null);

    // Sum quantities for same product
    const productMap = {};
    for (const p of products) {
      const name = p.name.trim();
      if (!name) continue;
      const qty = parseInt(p.quantity) || 0;
      if (!productMap[name]) {
        productMap[name] = 0;
      }
      productMap[name] += qty;
    }
    const mergedItems = Object.entries(productMap).map(([productName, quantity]) => ({
      productName,
      quantity
    }));

    try {
      const response = await axios.post(`${API_BASE_URL}/orders`, {
        items: mergedItems
      });
      setResult(response.data);
      onOrder && onOrder();
    } catch (err) {
      setResult(
        typeof err.response?.data === "object"
          ? err.response.data
          : { success: false, error: err.response?.data || "An error occurred." }
      );
    }
  };

  return (
    <div>
      <h2>Create Order</h2>
      <form onSubmit={handleSubmit}>
        {products.map((p, idx) => (
          <div key={idx}>
            <input
              name="name"
              value={p.name}
              onChange={e => handleChange(idx, e)}
              placeholder="Product name"
              required
            />
            <input
              name="quantity"
              type="number"
              value={p.quantity}
              onChange={e => handleChange(idx, e)}
              placeholder="Quantity"
              required
            />
            {products.length > 1 && (
              <button type="button" onClick={() => handleRemoveProduct(idx)}>-</button>
            )}
          </div>
        ))}
        <button type="button" onClick={handleAddProduct}>+ Add Product</button>
        <button type="submit">Submit Order</button>
      </form>
      {error && <div style={{ color: "red" }}>{error}</div>}
      {result && (
        <div style={{ color: result.success ? "green" : "red" }}>
          <div>Status: {result.success ? "SUCCESS" : "FAIL"}</div>
          {Array.isArray(result.route) && result.route.length > 0 && (
            <div>
              Route:{" "}
              {result.route.map((step, i) =>
                <span key={i}>({step.x}, {step.y}) </span>
              )}
            </div>
          )}
          {result.missingItems && typeof result.missingItems === 'object' && Object.keys(result.missingItems).length > 0 && (
            <div>
              Missing:{" "}
              {Object.entries(result.missingItems).map(([name, qty]) => (
                <span key={name}>
                  {name}: {qty}
                  {result.availableItems && typeof result.availableItems[name] !== "undefined"
                    ? ` (available: ${result.availableItems[name]})`
                    : ""}
                  {" "}
                </span>
              ))}
            </div>
          )}
          {typeof result === "object" && result.error && (
            <div>Error: {String(result.error)}</div>
          )}
        </div>
      )}
    </div>
  );
}
