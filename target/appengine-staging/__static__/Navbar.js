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
		e("div", { className: "navbar"},
			e("button", {onClick: handleSignOut}, "Déconnexion"),
			e("label", null, "Voir un profil"),
			e("input", {type: "text", ref: inputRef}, null),
			e("button", {onClick: handleProfil}, "Valider"),
			e("button", {onClick: handleUpload}, "Upload une photo"),
			e("button", {onClick: handleFeed}, "Accueil"),
			e("button", {onClick: handleMyProfil}, "Mon profil"),
		)
	)
}