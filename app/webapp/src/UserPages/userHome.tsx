import React, { useState, useEffect } from "react";
import { useCookies } from "react-cookie";
import { Link, useNavigate } from "react-router-dom";
import { getPaths, postPaths, jumps } from "../paths";
import { TopPlayers } from "./topPlayers";
import RainbowButton from "../start";

export function UserHome() {
    const [cookies, _ ,removeCookie] = useCookies(["user"])                               //cookies.get('auth') || 'Benfica'
    const user = { username: cookies.user.username, token : cookies.user.token}
    const [id, setId] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        async function getUser() {
            try {
    
                const rps = await fetch( getPaths.profile , {
                    method: "GET",
                    headers: { "Content-Type": "application/problem+json", Authorization: `Bearer ${user.token}` },
                });

                if (!rps.ok) {
                    throw new Error(`${rps.statusText} ${await rps.json()}`);
                }

                const rpsJson = await rps.json();
                setId(rpsJson.id);
            } catch (e) {
                console.log(e.message);
            }
        }
        getUser();
    }, [user.username, user.token]);

    const handleLogout = async () => {
        try {
            const response = await fetch(postPaths.logout, {
                method: 'POST',
                headers: {
                'Content-Type': 'application/problem+json', Authorization: `Bearer ${user.token}`
                },
                body: JSON.stringify({ username: user.username, token: user.token })
            });
        
            if (!response.ok) {
                throw new Error(response.statusText);
            }
            
            // Remover o cookie no sucesso do logout
            removeCookie('user', { path: '/' });
            navigate(jumps.home)
        } catch (error) {
            console.error('Erro ao fazer logout:', error);
        }
    };

    return (
        <div>
            <h1>Gomoku</h1>
            <h2>Lobby</h2>
            <p>»» Welcome <strong>{user.username}</strong> !</p>
            <p>
                <Link to={jumps.home}>
                    <RainbowButton onClick={handleLogout}>Logout</RainbowButton>
                </Link>
                <Link to={jumps.play.replace(':id',id)}>
                    <RainbowButton>Play Gomoku</RainbowButton> 
                </Link>
                <Link to={jumps.profile}>
                    <RainbowButton>Profile</RainbowButton>
                </Link>
            </p>
            < TopPlayers />
        </div>
    );
}