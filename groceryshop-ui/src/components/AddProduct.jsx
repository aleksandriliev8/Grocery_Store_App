import React, { useState } from "react";
import axios from "axios";

const API_BASE_URL = "https://localhost:8443";

export default function AddProduct({ onAdd }) {
  const [form, setForm] = useState({
    name: "",
    price: "",
    quantity: "",
    x: "",
    y: "",
  });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
    setSuccess("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");
    // Basic validation
    if (!form.name || !form.price || !form.quantity || form.x === "" || form.y === "") {
      setError("Please fill in all fields.");
      return;
    }
    try {
      const response = await axios.post(`${API_BASE_URL}/products`, {
        name: form.name,
        price: parseFloat(form.price),
        quantity: parseInt(form.quantity),
        location: { x: parseInt(form.x), y: parseInt(form.y) }
      });
      if (response.status === 201 || response.status === 200) {
        alert("Product created successfully!");
      }
      setForm({ name: "", price: "", quantity: "", x: "", y: "" });
      onAdd();
    } catch (err) {
      setError(err.response?.data || "An error occurred.");
    }
  };

  return (
    <div style={{marginBottom: "2em"}}>
      <h2>Add new product</h2>
      <form onSubmit={handleSubmit}>
        <input
          name="name"
          placeholder="Name"
          value={form.name}
          onChange={handleChange}
        />{" "}
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
        <input
          name="x"
          type="number"
          placeholder="Location X"
          value={form.x}
          onChange={handleChange}
        />{" "}
        <input
          name="y"
          type="number"
          placeholder="Location Y"
          value={form.y}
          onChange={handleChange}
        />{" "}
        <button type="submit">Add</button>
      </form>
      {error && <div style={{color:"red"}}>{error}</div>}
      {success && <div style={{color:"green"}}>{success}</div>}
    </div>
  );
}
