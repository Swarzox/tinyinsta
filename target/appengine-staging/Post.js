export default function Post({currentProfil, userId}) {
	
	const e = React.createElement
	const [post, setPost] = React.useState([]);
	const [userLikes, setUserLikes] = React.useState([]);
	const likesCount = React.useRef([]);
	
	React.useEffect( () => {
		fetch("https://web-cloud-datastore-363112.ew.r.appspot.com/_ah/api/myApi/v1/userPost/"+currentProfil)
			.then((response) => {
				console.log(response);
				if(response.status != 200) {
					return;
				}
				return response.text();
			})
			.then((data) => {
				if(data) {
					const json = JSON.parse(data).items;
					const posts = [];
					json.forEach((e, i) => {
						const val = e.properties;
						const fetchPost = {url: val.url, date: val.date, key: val.id, likes: val.likes, description: val.description}
						posts[i] = fetchPost;
					});
					setPost(posts);	
				} else {
					setPost([]);	
				}
			});
		fetch("https://web-cloud-datastore-363112.ew.r.appspot.com/_ah/api/myApi/v1/userLikes/"+userId)
			.then((response) => {
				if(response.status != 200) {
					return;
				}
				return response.text();
			})
			.then((data) => {
				if(data) {
					const json = JSON.parse(data).items;
					const likes = [];
					json.forEach((e, i) => {
						likes[i] = e;
					});
					setUserLikes(likes);	
				} else {
					setUserLikes([]);	
				}
			});
	}, [currentProfil]);
	
	function handleLike(key, i) {
		fetch("https://web-cloud-datastore-363112.ew.r.appspot.com/_ah/api/myApi/v1/likePost/"+key)
			.then((response) => {
				if(response.status != 200) {
					return;
				}
				return response.text();
			})
			.then((data) => {
				if(data) {
					setUserLikes([key, ...userLikes]);
					const val = parseInt(likesCount.current[i].textContent)
					likesCount.current[i].innerHTML = val+1
				}
			});
	}
	
	function handleDislike(key, i) {
		fetch("https://web-cloud-datastore-363112.ew.r.appspot.com/_ah/api/myApi/v1/unlikePost/"+key)
			.then((response) => {
				if(response.status != 200) {
					return;
				}
				return response.text();
			})
			.then((data) => {
				if(data) {
					setUserLikes(userLikes.filter((key) => !userLikes.includes(key)));
					const val = parseInt(likesCount.current[i].textContent)
					likesCount.current[i].innerHTML = val-1
				}
			});
	}
	
	return (
		e("div", { className: "containerPost"}, 
		post.length > 0 ? 
		post.map((img,i) => {
		  return(
			  e("div", {className: "containerImg", key: img.key},
				  e("img", {
					src: img.url,
					key: img.key
				  }, null),
				  e("button", {key: img.url, onClick: () => userLikes.includes(img.key) ? handleDislike(img.key, i) : handleLike(img.key, i)}, userLikes.includes(img.key) ? "Retirer like" : "Mettre like"),
				  e("p", {key: i, ref: el => likesCount.current[i] = el}, img.likes),
				  e("p", null, "Description : "+img.description)
			  )
		  )
		}) : e("p", null, "Aucune photo")
		)
	)
}