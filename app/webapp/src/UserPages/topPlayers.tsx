import React, {useEffect, useState} from "react";
import { getPaths, jumps } from '../paths';
import { Link } from "react-router-dom";

export function TopPlayers(){
    const [topPlayers, setTopPlayers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function fetchTopPlayers() {
            try {
                const response = await fetch(getPaths.topPlayers, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/problem+json',
                    },
                });

                if (!response.ok) {
                    throw new Error(response.statusText);
                }

                const data = await response.json();
                const topPlayers = data.topTen;
                setTopPlayers(topPlayers); // Supondo que a resposta da API seja um array de top jogadores
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        }

        fetchTopPlayers();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <div>
            <h2>Top Jogadores</h2>
            <ul>
                {topPlayers.map((player) => (
                    <li key={player.id}>
                        <Link to={jumps.playerInfo.replace(':name',player.username)}>  
                            <strong>{player.username}</strong> - Wins: {player.wins}
                        </Link>
                    </li>
                ))}
            </ul>
        </div>
    );
}

//jumps.playerInfo+"/"+player.username