export default function ProductCard({ product }) {
  return (
    <div className="border p-4 shadow rounded">
      <h3 className="font-bold">{product.name}</h3>
      <p className="text-sm">{product.description}</p>
      <p className="mt-2 font-semibold">₹{product.price}</p>
      <button className="mt-3 bg-green-600 text-white px-3 py-1">
        Add to Cart
      </button>
    </div>
  );
}
