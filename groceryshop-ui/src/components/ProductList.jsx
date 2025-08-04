import React, { useEffect, useState } from "react";
import axios from "axios";
import AddProduct from "./AddProduct";
import EditProduct from "./EditProduct";

const API_BASE_URL = "https://localhost:8443";

export default function ProductList() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [deleteError, setDeleteError] = useState("");
  const [editProduct, setEditProduct] = useState(null);

  const fetchProducts = () => {
    setLoading(true);
    axios
      .get(`${API_BASE_URL}/products`)
      .then((res) => {
        setProducts(Array.isArray(res.data) ? res.data : []);
        setLoading(false);
      })
      .catch(() => {
        setError("Error loading products.");
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const handleDelete = (id) => {
    if (!window.confirm("Are you sure you want to delete this product?")) return;
    setDeleteError("");
    axios
      .delete(`${API_BASE_URL}/products/${id}`)
      .then(() => fetchProducts())
      .catch(() => setDeleteError("Could not delete product."));
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;

  return (
    <div>
      <AddProduct onAdd={fetchProducts} />
      {editProduct && (
        <EditProduct
          product={editProduct}
          onSave={() => {
            setEditProduct(null);
            fetchProducts();
          }}
          onCancel={() => setEditProduct(null)}
        />
      )}
      <h2>Products</h2>
      {deleteError && <div style={{ color: "red" }}>{deleteError}</div>}
      {products.length === 0 ? (
        <p>No products available.</p>
      ) : (
        <table border="1" cellPadding="5">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Price</th>
              <th>Quantity</th>
              <th>Location</th>
              <th></th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {products.map((p) => (
              <tr key={p.id}>
                <td>{p.id}</td>
                <td>{p.name}</td>
                <td>{p.price}</td>
                <td>{p.quantity}</td>
                <td>
                  ({p.location.x}, {p.location.y})
                </td>
                <td>
                  <button onClick={() => setEditProduct(p)}>Edit</button>
                </td>
                <td>
                  <button onClick={() => handleDelete(p.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
