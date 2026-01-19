import { useEffect, useState } from "react";
import api from "../../api/axios";
import ProductCard from "../common/ProductCard";

export default function ProductList() {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    api.get("/products").then(res => setProducts(res.data));
  }, []);

  return (
    <div className="grid grid-cols-4 gap-4 p-6">
      {products.map(p => (
        <ProductCard key={p.id} product={p} />
      ))}
    </div>
  );
}
