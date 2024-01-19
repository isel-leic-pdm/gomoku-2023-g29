import React, { useEffect } from "react"; 
import { Link, Outlet, useNavigate } from "react-router-dom";
import { jumps } from "./paths";
import { TopPlayers } from "./UserPages/topPlayers";
import RainbowButton from "./start";
import { useCookies } from "react-cookie";

export function Home(){
    const [cookies] = useCookies(["user"]);
    const navigate = useNavigate();

    useEffect(() => {
        if (cookies.user) {
            navigate(jumps.sweetHome);
        }
    }, [cookies.user, navigate]);

    
    return (
        <div>
            <h1>Gomoku</h1>
            <h2>Home</h2>
            <div style={{
                display: "flex",
                justifyContent: "space-between",
                margin: "auto",
            }}>
                <Link to={jumps.login}> <RainbowButton>Login</RainbowButton></Link>
                <Link to={jumps.signup}> <RainbowButton>SignUp</RainbowButton></Link>
            </div>
            < TopPlayers />
            < Outlet />
        </div>
    )
}