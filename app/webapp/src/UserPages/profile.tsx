import React, { useState, useEffect } from "react";
import { useCookies } from "react-cookie";
import { Link } from "react-router-dom";
import { getPaths, jumps } from "../paths";
import RainbowButton from "../start";


export function Profile(){
    const [cookies] = useCookies(["user"])                               //cookies.get('auth') || 'Benfica'
    const user = { username: cookies.user.username, token : cookies.user.token, id: cookies.user.id}
    const [wins, setWins] = useState(0);
    const [njogos, setJogos] = useState(0);

    useEffect(() => {
        async function getIdAndPlayerRank(){
            try {
                const rps2 = await fetch( getPaths.userRank + user.username, {
                    method: "GET",
                    headers: { "Content-Type": "application/problem+json" },
                });
                
                if (!rps2.ok) {
                    throw new Error(`${rps2.statusText} ${await rps2.json()}`);
                }

                const rps2Json = await rps2.json();
                setWins(rps2Json.wins);
                setJogos(rps2Json.njogos);

            } catch (e) {
                console.log(e.message);
            }
        }
        getIdAndPlayerRank();
    }, [user.username, user.token, user.id])

    return (
        <div>
            <h1>Gomoku</h1>
            <h2>Player details</h2>
                <p><u> Player ID = {user.id} </u></p>
                <p><strong>{user.username}</strong> has Won <strong>{wins}</strong> out of <strong>{njogos}</strong> Games!</p>
            <p><Link to={jumps.sweetHome}><RainbowButton>← Go back</RainbowButton></Link> </p>
        </div>
    )
}