import { useEffect } from "react";

export default function Home() {
  useEffect(() => {
    fetch("http://localhost:8080/test")
      .then(res => res.text())
      .then(data => console.log(data));
  }, []);

  return <h1>Frontend + Backend Connected 🚀</h1>;
}
