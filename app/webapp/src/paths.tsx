
const host = 'http://localhost:8080'
const domains = {
    users : `${host}/users`,
    games : `${host}/games`
}
export const getPaths = {
    userById: `${domains.users}/id/`,       // +user_id
    userByName: `${domains.users}/`,        // +user_name
    userRank: `${domains.users}/rank/`,     // +user_name
    topPlayers: `${domains.users}/top10`,
    profile: `${domains.users}/sweetHome`,

    gameById: `${domains.games}/id/`,       // +game_id
    gameByUser: `${domains.games}/users/`,  // +user_id
}

export const postPaths = {
    login: `${domains.users}/login`,        // +body
    logout: `${domains.users}/logout`,      // +body
    signup: `${domains.users}/createUser`,  // +body

    startGame: `${domains.games}/start`,    // +body
    playPiece: `${domains.games}/piece/`,   // +game_id +body
}

export const jumps = {
    login: '/login',
    signup: '/createUser',
    profile: '/profile',
    sweetHome: '/sweetHome',
    playerInfo: '/playerInfo/:name',
    wait: '/waitqueue/:traditional',
    game: '/game/:id',
    play: '/play/:id',
    topPlayers: 'topPlayers',
    home: '/'
}
