
import React, {useState} from 'react'
import { Link, Navigate } from 'react-router-dom';
import {jumps, postPaths} from "../paths";
import RainbowButton from '../start';
import { fetchError } from '../GamePages/game';

export function SignUp() {
    const [inputs, setInputs] = useState({ username: '', password: '' });
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState('');
    const [redirect, setRedirect] = useState(false);

    if (redirect) return <Navigate to={jumps.home} replace={true} />;

    async function handleSubmit(ev: React.FormEvent<HTMLFormElement>) {
        ev.preventDefault();
        setSubmitting(true);

        const username = inputs.username;
        const password = inputs.password;

        try {
            const response = await fetch( postPaths.signup , {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/problem+json',
                },
                body: JSON.stringify({ username, password }),
            });

            if (!response.ok) {
                const data = await response.json();
                console.log(data);
                if (fetchError(data.type) == "INSECURE PASSWORD") {
                    throw new Error("Insecure Password (Your password is too weak)\n"+
                    "Try to use a password that:\n"+
                     "- is at least 4 characters long\n"+
                     "- has at least 1 upper case letter\n"+
                     "- has at least 1 lower case letter\n"+
                     "- has at least 1 number\n"+
                     "- has at least 1 special character\n\n"+
                    "Example: aB1!");
                }
            }
            setRedirect(true);
        } catch (error) {
            setError(error.message);
        } finally {
            setSubmitting(false);
        }
    }

    function handleChange(ev: React.FormEvent<HTMLInputElement>) {
        const name = ev.currentTarget.name;
        setInputs({ ...inputs, [name]: ev.currentTarget.value });
    }

    return (
        <div>
            <h1>Gomoku</h1>
            <h2>SignUp</h2>
            <form onSubmit={handleSubmit}>
                <fieldset disabled={submitting}>
                    <div>
                        <label htmlFor="username">Username</label>
                        <input
                            id="username"
                            type="text"
                            name="username"
                            value={inputs.username}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <label htmlFor="password">Password</label>
                        <input
                            id="password"
                            type="password"
                            name="password"
                            value={inputs.password}
                            onChange={handleChange}
                        />
                    </div>
                    <div>
                        <RainbowButton type="submit">SignUp</RainbowButton>
                    </div>
                </fieldset>
            </form>
            <p><Link to={jumps.home}><RainbowButton>← Go back</RainbowButton></Link></p>
            <div>
                {error && <pre>{error}</pre>}
            </div>
        </div>
    );
}