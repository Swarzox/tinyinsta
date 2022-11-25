import Post from "./Post.js";

export default function Profil({currentProfil, userId, setUpload, handleSignOut, setCurrentProfil}) {
	
	const e = React.createElement
	const [following, isFollowing] = React.useState(false);
	const [profilId, setProfilId] = React.useState(null);
	
	function handleFollow() {
		const action = following ? "unfollowUser" : "followUser"
		fetch("https://web-cloud-datastore-363112.ew.r.appspot.com/_ah/api/myApi/v1/"+action+"/"+profilId.replace(/['"]+/g, ''))
			.then((response) => {
				if(response.status != 200) {
					return;
				}
				return response.text();
			})
			.then((data) => {
				console.log(data);
				if(data) {
					isFollowing(!following);
				}
			});		
	}
	
	React.useEffect( () => {
			if(currentProfil.length == 21 && currentProfil.match(/^[0-9]+$/) != null) {
				setProfilId(currentProfil);
				return;
			} 
			fetch("https://web-cloud-datastore-363112.ew.r.appspot.com/_ah/api/myApi/v1/userIdByName/"+currentProfil)
			.then((response) => {
				if(response.status != 200) {
					setProfilId(null)
					return;
				}
				return response.text();
			})
			.then((data) => {
				if(data) {
					setProfilId(data);
				} else {
					setProfilId(null)
				}
			});	
	}, [currentProfil]);
	
	React.useEffect( () => {
		if(profilId) {
			fetch("https://web-cloud-datastore-363112.ew.r.appspot.com/_ah/api/myApi/v1/isFollowing/"+profilId.replace(/['"]+/g, ''))
				.then((response) => {
					if(response.status != 200) {
						return;
					}
					return response.text();
				})
				.then((data) => {
					if(data) {
						isFollowing(true);
					}
				});	
		}
	}, [profilId]);
	
	function handleUpload() {
		setCurrentProfil("");
		setUpload(true);
	}
	
	return (
		e("div", { className: "w-full flex justify-center"},
			profilId ? e("div", {className: "w-full"},
				e("div", {className: "flex justify-center"},
				currentProfil != userId && e("label", {className: "text-xl mr-4 font-bold"}, currentProfil),
				currentProfil != userId && e("button", {onClick: handleFollow, className: "bg-blue-300 px-2 py-1 text-white rounded-md"}, following ? "Se désabonner" : "S'abonner"),	
				currentProfil == profilId &&  e("button", {onClick: handleSignOut, className: "bg-red-300 px-2 py-1 text-white rounded-md mr-2"}, "Déconnexion"),	
				currentProfil == profilId &&  e("button", {onClick: handleUpload, className: "bg-blue-300 px-2 py-1 text-white rounded-md"}, "Ajouter une photo"),	
			),
				e(Post, {currentProfil: profilId.replace(/['"]+/g, ''), name: currentProfil, userId}, null)
			) :
			e("p", null, "profil introuvable")
		)
	)
}