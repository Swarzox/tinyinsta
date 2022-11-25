import Profil from "./Profil.js";
import Upload from "./Upload.js";

export default function Navbar({setCurrentProfil, userId, setUpload, setFeed, setUser}) {
	
	const e = React.createElement
	
	const inputRef = React.useRef(null);
	
	function handleSignOut(e) {
		setUser({});
		fetch("https://web-cloud-datastore-363112.ew.r.appspot.com/disconnection")
			.then((res) => {
				console.log(res);
			})
	}
	
	function handleProfil() {
		if(inputRef.current != null && inputRef.current.value != "") {
			setUpload(false)
			setFeed(false)
			setCurrentProfil(inputRef.current.value)
		}
	}
	
	function handleUpload() {
		setCurrentProfil("")
		setFeed(false)
		setUpload(true)
	}
	
	function handleMyProfil() {
		setUpload(false)
		setFeed(false)
		setCurrentProfil(userId)
	}
	
	function handleFeed() {
		setUpload(false)
		setCurrentProfil("")
		setFeed(true)
	}
	
	return (
		e("div", { className: "flex items-center justify-around flex-wrap bg-white p-6 shadow-md mb-20"},
			e("div", {className: "flex items-center flex-shrink-0 text-gray-800 mr-6 cursor-pointer"},
				e("img", {src: "https://cdn-icons-png.flaticon.com/512/87/87390.png", className:"w-10 h-10", onClick: handleFeed}, null)
			),
			//e("button", {onClick: handleSignOut, className: "bg-gray-200 rounded-xl px-4 py-2 hover:bg-gray-300"}, "Déconnexion"),
			e("div", {className: "flex items-center gap-3"},
			e("input", {type: "text", ref: inputRef, placeHolder: "Rechercher un pseudo...", className: "text-sm w-64 bg-gray-100 focus:outline-none focus:shadow-outline border border-gray-400 rounded py-1 px-4 block w-full appearance-none leading-normal"}, null),
			e("button", {onClick: handleProfil, className: "gg-search"}, null),
			),
			//e("button", {onClick: handleUpload, className: "bg-gray-200 rounded-xl px-4 py-2 hover:bg-gray-300"}, "Upload"),
			//e("button", {onClick: handleFeed, className: "bg-gray-200 rounded-xl px-4 py-2 hover:bg-gray-300"}, "Feed"),
			e("button", {onClick: handleMyProfil, className: "gg-profile"}, null),
		)
	)
}