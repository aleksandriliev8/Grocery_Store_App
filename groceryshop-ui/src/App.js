import React from "react";
import ProductList from "./components/ProductList";
import AddOrder from "./components/AddOrder";
import OrderList from "./components/OrderList";

function App() {
  return (
    <div style={{ padding: "2em" }}>
      <h1>Grocery Shop Admin UI</h1>
      <ProductList />
      <hr />
      <AddOrder />
      <OrderList />
    </div>
  );
}

export default App;
