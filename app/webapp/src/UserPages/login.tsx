import React, { useState } from "react";
import { useCookies } from "react-cookie";
import { Link, Navigate } from "react-router-dom";
import RainbowButton, { User } from "../start";
import { getPaths, jumps, postPaths } from "../paths";



export function Login(){
    const [cookies, setCookie] = useCookies(["user"])                               //cookies.get('auth') || 'Benfica'
    const [inputs, setInputs] = useState({ username: null, password: null })
    const [submitting, setSubmitting] = useState(false)
    const [error, setError] = useState('')
    const [redirect, setRedirect] = useState(false);
    //const navigate = useNavigate()

    if (redirect) return <Navigate to={jumps.sweetHome} replace={true} state={{ username: inputs.username, token: cookies.user.token }} />
       //<Navigate to="/sweetHome" replace={true} />;<Lobby username={cookies.user.username} token={cookies.user.token} />

    async function handleSubmit(ev: React.FormEvent<HTMLFormElement>) {
        ev.preventDefault()
        setSubmitting(true)
        const username = inputs.username
        const password = inputs.password
        try{
            const rps = await fetch (postPaths.login,{
                method: "POST",
                headers: {"Content-Type": "application/problem+json"},
                body: JSON.stringify({username, password})
            });
            if(!rps.ok)
                throw new Error(rps.statusText);
            const rpsJson = await rps.json();

            const rpsId = await fetch(getPaths.profile , {
                method: "GET",
                headers: { "Content-Type": "application/problem+json", Authorization: `Bearer ${rpsJson.token}` },
            });
            if (!rpsId.ok) 
                throw new Error(`${rpsId.statusText} ${await rpsId.json()}`);
            const rpsJsonId = await rpsId.json();

            const userClass : User = {username: inputs.username, token: rpsJson.token, id: rpsJsonId.id}
            setCookie("user", userClass, { path: '/' });
            
            setRedirect(true);
        } catch (e) {
            setError(e.message)
        } finally {
            setSubmitting(false)
        }
    }

    function handleChange(ev: React.FormEvent<HTMLInputElement>) {
        const name = ev.currentTarget.name
        setInputs({ ...inputs, [name]: ev.currentTarget.value })
    }

    return (
        <div>
            <h1>Gomoku</h1>
            <h2>Login</h2>
            <form onSubmit={handleSubmit}>
                <fieldset disabled={submitting}>
                    <div>
                        <label htmlFor="username">Username</label>
                        <input 
                            id="username" 
                            type="text" 
                            name="username" 
                            value={inputs.username} 
                            onChange={handleChange} />
                    </div>
                    <div>
                        <label htmlFor="password">Password</label>
                        <input 
                        id="password" 
                        type="password" 
                        name="password" 
                        value={inputs.password} 
                        onChange={handleChange} />
                    </div>
                    <div>
                        <RainbowButton type="submit">Login</RainbowButton>
                    </div>
                </fieldset>
            </form>
            <p><Link to={jumps.home}><RainbowButton>← Go back</RainbowButton></Link></p>
            {error}
        </div>
    )
}
