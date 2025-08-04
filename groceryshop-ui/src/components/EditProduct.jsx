import React, { useState } from "react";
import axios from "axios";

const API_BASE_URL = "https://localhost:8443";

export default function EditProduct({ product, onSave, onCancel }) {
  const [form, setForm] = useState({
    price: product.price,
    quantity: product.quantity,
  });
  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError("");
    try {
      await axios.put(`${API_BASE_URL}/products/${product.id}`, {
        price: parseFloat(form.price),
        quantity: parseInt(form.quantity),
      });
      setSaving(false);
      onSave();
    } catch (err) {
      setSaving(false);
      setError(err.response?.data || "Error updating product.");
    }
  };

  return (
    <div style={{margin: "2em 0"}}>
      <h2>Edit product: {product.name}</h2>
      <form onSubmit={handleSubmit}>
        <input
          name="price"
          type="number"
          step="0.01"
          placeholder="Price"
          value={form.price}
          onChange={handleChange}
        />{" "}
        <input
          name="quantity"
          type="number"
          placeholder="Quantity"
          value={form.quantity}
          onChange={handleChange}
        />{" "}
        <button type="submit" disabled={saving}>Save</button>
        <button type="button" onClick={onCancel} style={{marginLeft:10}}>
          Cancel
        </button>
      </form>
      {error && <div style={{color:"red"}}>{error}</div>}
    </div>
  );
}
