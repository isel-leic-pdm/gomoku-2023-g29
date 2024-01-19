import React, { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { getPaths, jumps, postPaths } from "../paths";
import { useCookies } from "react-cookie";
import RainbowButton from "../start";

interface Gomoku {
    id: string;
    board: Board;
    userId1: string;
    userId2: string;
    isFinished: boolean;
    winner: string;
}

interface Board {
    gamesize: boolean;
    cells: string[][];
    size: number;
}

const gitErrors = "https://github.com/isel-leic-daw/2023-daw-leic53d-2023-daw-leic53d-g06/tree/main/code/jvm/Gomoku/docs/problems/"
const crossLink = "https://cdn.discordapp.com/attachments/1155469158948016178/1156541951483580476/59565.png?ex=6582c5a0&is=657050a0&hm=346a0bd306afac475ee29a4d9677c7601da78f90488f14cee4f14ccd91149fcd&"
const playerJuan = "https://cdn.discordapp.com/attachments/1155469114068971530/1183544171626242158/red.png?ex=6588b867&is=65764367&hm=95bc05f4233a585aec1a3886481ac0a9c5b988e67ffbbdbfcebffaa79af72f0d&"
const playerShoe = "https://cdn.discordapp.com/attachments/1155469114068971530/1183543540551262209/pixel_ball_test_by_rongs1234_d7g8n34.png?ex=6588b7d1&is=657642d1&hm=d7791858d5f539df5d7ba07393780b52b68037920a48c84ab677b664f0af9457&"

export function fetchError( weburl :String) {
    const url = weburl.replace(gitErrors, "");
    return url.replace(/-/g, " ").toUpperCase();
    /*
    const url = weburl.replace('github.com', 'raw.githubusercontent.com');
    const response = await fetch(url, {
        headers: {
            'Authorization': `token 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`
        }
    });
    const data = await response.text();
    console.log(data);
    return data;
    */
};


export function Game() {
    // Get game id from URL parameters
    const { id } = useParams()

    // Initialize state variables
    //const [currentPlayer, setCurrentPlayer] = useState(null);
    const [gomoku, setGomoku] : [Gomoku, React.Dispatch<Gomoku>] = useState(null);
    const [turn, setTurn] = useState(null);
    const [currentMove, setCurrentMove] = useState(null);
    const [error, setError] = useState('');

    // Get user information from cookies
    const [cookies] = useCookies(["user"])
    const user = { username: cookies.user.username, token : cookies.user.token, id: cookies.user.id };


    // Function to confirm placement of a piece
    const confirmPlacement = async () => {
        if (currentMove && !gomoku.isFinished && id !== undefined) {
            try {
                if(turn !== user.id){
                    throw new Error("It's not your turn!");
                }

                const response = await fetch(postPaths.playPiece + id, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/problem+json' },
                    body: JSON.stringify(currentMove),
                });
                const data = await response.json();
                switch(response.status){
                    case 200:
                    case 201:
                        break;
                    case 301:
                    case 400:
                    case 404:
                        throw new Error(fetchError(data.type));
                    default:
                        throw new Error(response.statusText);
                }
                setError("");

                setGomoku(data);
                console.log(gomoku)
                setTurn(turn === gomoku.userId1 ? gomoku.userId2 : gomoku.userId1);
                fetchBoard();
                setCurrentMove(null); // Clear the current move
            } catch (error) {
                setError(error.message);
                console.error('Failed to confirm placement:', error);
            }
        }
    };

    // updates the board automatically
    const fetchBoard = async () => {
        try {
            const response = await fetch(getPaths.gameById+id, {
                method: 'GET',
                headers: { 'Content-Type': 'application/problem+json' }
            });
            if (!response.ok) 
                throw new Error(`${response.statusText} ${await response.json()}`);

            const data = await response.json();
            const dataToGomoku = data as Gomoku;
            setGomoku(dataToGomoku);
            if(turn === null){
                const pieces = countPieces(dataToGomoku.board);
                pieces.player1Pieces > pieces.player2Pieces ?
                 setTurn(dataToGomoku.userId2) : setTurn(dataToGomoku.userId1);
            }
            //setBoard(gomoku.board);
     
        } catch (error) {
            setError(error.message);
            console.error('Failed to fetch board:', error);
        }
    };

    const makeMove = async (col, row) => {
        setCurrentMove({ 
            userId: user.id,
            l : row, 
            c : col
        });
    };

    useEffect(() => {
        const errorCleaner = setInterval(() => setError(""), 5000);
        return () => clearInterval(errorCleaner);
    }, []);

    useEffect(() => {
        const timer = setInterval(fetchBoard, 1800);
        return () => clearInterval(timer);
    }, []);

    return(
        <div>
            <h1>Gomoku</h1>
            {gomoku === null ? (
                    <><p>Loading...</p><RainbowButton onClick={() => fetchBoard()}>Force Refresh</RainbowButton></>
                ) : (
                    gomoku.isFinished ? (
                        <div>
                            <>{gomoku.winner == user.id ? (<p>You WON!!!</p>) : (<p>You Lost...</p>) }</>
                            <Link to={jumps.sweetHome}><RainbowButton>Return to Lobby</RainbowButton></Link>
                        </div>
                    ) : (
                    <div style={{ display: 'flex',flexDirection: 'column', justifyContent: 'center', alignItems: 'center' }}>
                        <h3>{turn === user.id ? "It's your turn!" : "Not your turn yet..."}</h3>
                        <div style= {{display: 'flex', flexDirection: 'column' }}>
                            {gomoku.board.cells.map((row, i) => (
                                <div 
                                    key={i} 
                                    style={{ 
                                        margin: 0, 
                                        padding: 0, 
                                        boxShadow:'none',
                                        lineHeight: 0,
                                    }}
                                >
                                    {row.map((cell, j) => (
                                        <button 
                                            key={j} 
                                            onClick={() => makeMove(i, j)}
                                            style={{
                                                display: 'inline grid',
                                                border: 'none',
                                                padding: 0,
                                                width: '20px',
                                                height: '20px',
                                                backgroundImage: `url(${cell === EMPTY ? crossLink : cell === P1 ? playerJuan : playerShoe})`,
                                                backgroundSize: 'contain',
                                                backgroundRepeat: 'no-repeat',
                                                lineHeight: 0,
                                                borderRadius: '0px',
                                            }}
                                            ></button>
                                    ))}
                                </div>
                            ))}
                        </div>
                        <RainbowButton onClick={confirmPlacement}>Confirm Placement</RainbowButton>
                    </div>          
                    )
                )
            }
            <p>{error}</p>
        </div>
    );
}

//{cell === "EMPTY" ? EMPTY : cell === "PLAYER_ONE" ? P1 : P2}

const P1 = "PLAYER_ONE";
const P2 = "PLAYER_TWO";
const EMPTY = "EMPTY";

const countPieces = (board) => {
    let player1Pieces = 0;
    let player2Pieces = 0;

    for (let row of board.cells) {
        for (let cell of row) {
            if (cell === P1) player1Pieces++;
            if (cell === P2) player2Pieces++;
        }
    }

    return { player1Pieces, player2Pieces };
};
