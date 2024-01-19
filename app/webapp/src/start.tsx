import {
    BrowserRouter,
    createBrowserRouter,
    RouterProvider,
    Routes} from "react-router-dom";
import React, { useEffect } from 'react'
import { createRoot } from 'react-dom/client'
import { SignUp } from './UserPages/signup'
import { jumps } from './paths'
import { Login } from "./UserPages/login";
import { TopPlayers } from './UserPages/topPlayers'
import { Play } from './GamePages/play'
import { Profile } from './UserPages/profile'
import { Home } from './home'
import { UserHome } from "./UserPages/userHome";
import { PlayerShow } from "./UserPages/PlayerDetails";
import { Game } from "./GamePages/game";
import { Wait } from "./GamePages/wait";

// ...

const router = createBrowserRouter(
    [
        {
            path : jumps.home, // pagina do user
            element : < Home />,
            children: [
                {
                    path: jumps.topPlayers,
                    element: < TopPlayers />,
                }
            ],
        },
        {
            path : jumps.sweetHome, // pagina do user 
            element : < UserHome />,
            children: [
                {
                    path: jumps.topPlayers,
                    element: < TopPlayers />,
                }
            ],
        },
        {
            path : jumps.login, // pagina de login
            element : < Login />
        },
        {
            path : jumps.signup, // pagina de signup
            element : < SignUp />
        },
        {
            path : jumps.profile, // pagina do jogador
            element : < Profile />
        },
        {
            path : jumps.play,  // pagina de escolha do modo de jogo
            element : < Play />
        },
        {
            path : jumps.playerInfo, // pagina do jogador
            element : < PlayerShow />
        },
        {
            path : jumps.game, // pagina do jogo
            element : < Game />
        },
        {
            path: jumps.wait, // pagina de espera do jogo
            element: < Wait />,
        }
    ]
)

export interface User {
    id: string,
    username: string,
    token: string
}

const RainbowButton = ({ children, ...props }) => (
    <button data-text={children} {...props}>{children}</button>
);
  
export default RainbowButton;

function Demo() {
    /*
    document.querySelectorAll('button').forEach(button => {
        button.setAttribute('data-text', button.textContent);
    });
    */
    
    useEffect(() => {
        const themeToggle = document.getElementById('theme-toggle') as HTMLButtonElement;
    
        if (themeToggle) {
            const toggleTheme = () => {
                document.body.classList.toggle('light-theme');
                const container = document.getElementById('container');
                if (container) {
                    container.classList.toggle('light-theme');
                }
            };
    
            // Attach the event listener
            themeToggle.addEventListener('click', toggleTheme);
    
            // Clean up the event listener when the component unmounts
            return () => {
                themeToggle.removeEventListener('click', toggleTheme);
            };
        }
    }, []);


    return (
        <RouterProvider router={router}>
            
        </RouterProvider>
    );
}

export function App() {
    const root = createRoot(document.getElementById("container"));
    root.render(<Demo/>);
}