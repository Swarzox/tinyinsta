import Post from "./Post.js";

export default function Profil({currentProfil, userId}) {
	
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
	
	return (
		e("div", { className: "containerProfil"},
			profilId ? e("div", {className: "ok"}, 
			    e("p", null, "profil de : " + profilId),
				e("button", {onClick: handleFollow}, following ? "Se désabonner" : "S'abonner"),
				e(Post, {currentProfil: profilId.replace(/['"]+/g, ''), userId}, null)
			) :
			e("p", null, "profil introuvable")
		)
	)
}