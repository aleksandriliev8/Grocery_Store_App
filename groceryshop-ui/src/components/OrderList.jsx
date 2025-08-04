import React, { useEffect, useState } from "react";
import axios from "axios";

const API_BASE_URL = "https://localhost:8443";

export default function OrderList() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const fetchOrders = () => {
    setLoading(true);
    axios
      .get(`${API_BASE_URL}/orders`)
      .then((res) => {
        setOrders(Array.isArray(res.data) ? res.data : []);
        setLoading(false);
      })
      .catch(() => {
        setError("Error loading orders.");
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  if (loading) return <p>Loading...</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;

  return (
    <div style={{ marginTop: 40 }}>
      <h2>Orders</h2>
      {orders.length === 0 ? (
        <p>No orders available.</p>
      ) : (
        <table border="1" cellPadding="5">
          <thead>
            <tr>
              <th>ID</th>
              <th>Created At</th>
              <th>Status</th>
              <th>Products</th>
              <th>Route</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order) => (
              <tr key={order.id}>
                <td>{order.id}</td>
                <td>{order.createdAt}</td>
                <td>{order.status}</td>
                <td>
                  {(order.items || []).map(item =>
                    `${item.productName || item.name} (${item.quantity})`
                  ).join(", ")}
                </td>
                <td>
                  <button type="button" onClick={() => window.alert(
                    Array.isArray(order.route)
                      ? order.route.map(step => `(${step.x},${step.y})`).join(" → ")
                      : "No route"
                  )}>
                    View Route
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
