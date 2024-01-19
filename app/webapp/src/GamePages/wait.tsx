import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { jumps, postPaths } from "../paths";
import { useCookies } from "react-cookie";

export function Wait(){
    const navigate = useNavigate();
    const [gameId, setGameId] = useState(null);
    const {traditional} = useParams()
    const [redirect, setRedirect] = useState(false);
    const [cookies] = useCookies(["user"]);
    const user = { username: cookies.user.username, token : cookies.user.token, id: cookies.user.id};

    if (redirect && gameId!==null) navigate(jumps.game.replace(":id",gameId));
   

    const fetchGame = async () => {
        try {
            const userId1 = user.id;
            const rps = await fetch( postPaths.startGame , {
                method: "POST",
                headers: { "Content-Type": "application/problem+json", Authorization: `Bearer ${user.token}` },
                body: JSON.stringify({ userId1, traditional }),
            });
            console.log(rps);
            if(!rps.ok)
                throw new Error(rps.statusText);

            const rspJson = await rps.json();
            console.log(rspJson.id);
            
            if (rps.ok) {
                setGameId(rspJson.id);
                setRedirect(true);
                 // game receives the game id this id is from user
            }
        } catch (error) {
            console.error('Error:', error);
        }
    };

    useEffect(() => {
        const timer = setInterval(fetchGame, 1800);
        return () => clearInterval(timer);
    }, []);

    return(
        <div>
            <h1>Game</h1>
            <p>Waiting for a player to join...</p>
        </div>
    )
}