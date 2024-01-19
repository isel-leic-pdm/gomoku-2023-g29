import React, { useState, useEffect } from "react";
import { useCookies } from "react-cookie";
import { Link, useParams } from "react-router-dom";
import { getPaths, jumps } from "../paths";
import RainbowButton from "../start";


export function PlayerShow(){
    const { name } = useParams()
    const [cookies] = useCookies(["user"])                               //cookies.get('auth') || 'Benfica'
    const user = cookies.user;
    //const user = { username: cookies.user.username, token : cookies.user.token}
    const [wins, setWins] = useState(0);
    const [jogos, setJogos] = useState(0);
    useEffect(() => {
        async function getId(){
            try {
                const rps2 = await fetch( getPaths.userRank + name, {
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
        getId();
    }, [name, user])

    const backButton = user ? (
        <Link to={jumps.sweetHome}>
            <RainbowButton>← Go back</RainbowButton>
        </Link>
    ) : (
        <Link to={jumps.home}>
            <RainbowButton>Back to Home</RainbowButton>
        </Link>
    );

    return (
        <div>
            <h1>Gomoku</h1>
            <h2>Player details</h2>
            <p><strong>{name}</strong> has Won <strong>{wins}</strong> out of <strong>{jogos}</strong> Games!</p> 
            {backButton}
        </div>
    )
    
}