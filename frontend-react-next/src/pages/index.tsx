import { useEffect, useState } from 'react';

export default function Home() {
  const [projetos, setProjetos] = useState([]);
  const [usuarios, setUsuarios] = useState([]);

  useEffect(() => {
    fetch('http://localhost:8080/api/projetos')
      .then(res => res.json())
      .then(data => setProjetos(data));

    fetch('http://localhost:8080/api/external/usuarios')
      .then(res => res.json())
      .then(data => setUsuarios(data));
  }, []);

  return (
    <div className="p-4">
      <h1 className="text-xl font-bold">Projetos</h1>
      <ul>
        {projetos.map((p: any) => (
          <li key={p.id}>{p.nome}</li>
        ))}
      </ul>

      <h2 className="text-lg font-semibold mt-4">Usuários Externos</h2>
      <ul>
        {usuarios.map((u: any) => (
          <li key={u.id}>
            {u.name} - {u.email}
          </li>
        ))}
      </ul>
    </div>
  );
}
