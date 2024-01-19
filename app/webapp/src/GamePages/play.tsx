import React, { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import { Link, useNavigate, useParams } from "react-router-dom";
import { Dropdown, Button } from 'react-bootstrap';
import { jumps, postPaths } from "../paths";
import RainbowButton from "../start";

export function Play() {
    const [error, setError] = useState('')
    const [gameMode, setGameMode] : [Number, React.Dispatch<Number>] = useState(null);
    
    const [cookies] = useCookies(["user"]); 
    const user = { username: cookies.user.username, token : cookies.user.token, id: cookies.user.id};

    const navigate = useNavigate();
    
    async function startOrJoinGame(){
        try {
            const traditional = gameMode === 15 ? true : false;
            console.log(gameMode);
            const userId1 = user.id;
            const rps = await fetch( postPaths.startGame , {
                method: "POST",
                headers: { "Content-Type": "application/problem+json", Authorization: `Bearer ${user.token}` },
                body: JSON.stringify({ userId1, traditional }),
            });
            
            if (rps.status === 201) {
                const json = await rps.json();
                navigate(jumps.game.replace(":id",json.id)); // navigate to the Game page
            } else if (rps.status === 400 || rps.status === 404) {
                navigate(jumps.wait.replace(":traditional",""+traditional)); // navigate to the Wait page
            } else {
                throw new Error(rps.statusText);
            }
        
        } catch (e) {
            console.error(e.message)
            setError(e.message);
        }
    }

    useEffect(() => {
        if (gameMode !== null) {
            startOrJoinGame();
        }
    }, [gameMode]);

    const handleSelect = (eventKey: number) => { 
        if(user.token === "" || user.token === undefined || user.token === null){ // Just for safety
            navigate(jumps.login);
        }
        setGameMode(eventKey);
    };
 
    return (
        <div>
            <h1>Gomoku</h1>
            <p>
                <Dropdown>
                    <Dropdown.Toggle variant="success" id="dropdown-basic" data-text="Choose your game mode here">
                        Choose your game mode here
                    </Dropdown.Toggle>
                    
                    <Dropdown.Menu>
                        <RainbowButton variant="outline-primary" onClick={() => handleSelect(15)} style={{ margin: '5px' }}>Traditional</RainbowButton>
                        <RainbowButton variant="outline-primary" onClick={() => handleSelect(19)} style={{ margin: '5px' }}>Extended</RainbowButton>
                    </Dropdown.Menu>
                </Dropdown>
            </p>
            <strong>Traditional mode:</strong> 15x15 board.
            <br></br>
            <strong>Extended mode:</strong> 19x19 board.
            <br></br>
            <strong>Rules:</strong> 5 in a row to win.
            <p>{error}</p>
            <p><Link to={jumps.sweetHome}><RainbowButton>← Go back</RainbowButton></Link> </p>
        </div>
    )
}

