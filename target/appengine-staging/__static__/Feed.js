export default function Feed() {
	
	const nbElt = 3;
	const [page, setPage] = React.useState(0);
	const [feed, setFeed] = React.useState([]);
	const e = React.createElement
	
	React.useEffect(() => {
		fetch("https://web-cloud-datastore-363112.ew.r.appspot.com/_ah/api/myApi/v1/feedUser")
			.then((res) => {
				if(res.status != 200) {
					return;
				}
				return res.text();
			}).then((data) => {
				if(data) {
					const arr = JSON.parse(data).items;
					const posts = [];
					for(let i = 0; i < arr.length; i++) {
						const prop = arr[i].properties;
						posts[i] = prop;
					}
					setFeed(posts);
				} else {
					setFeed([]);
				}
			});
	},[]);
	
	return (
		e("div", {className: "container"},
			e("div", { className: "containerFeed"}, 
			feed.length > 0 ? 
			feed.map((img,i) => {
			  return(
				  i >= page*nbElt && i < (page+1)*nbElt ?
				  e("div", {className: "containerImg", key: img.id},
					  e("img", {
						src: img.url,
						key: img.id
					  }, null),
					  e("p", null, "Description : "+img.description),
					  e("p", null, "Date : "+img.date),
					  e("p", null, "Likes : "+img.likes)
				  ) :
				  null
			  )
			}) : e("p", null, "Aucune photo")
			),
			e("div", {className: "containerBtn"},
				e("button", {onClick: () => setPage(page-1), disabled: page==0}, "Page suivante (<)"),
				e("label", null, "Page " + parseInt(page+1)),
				e("button", {onClick: () => setPage(page+1), disabled: (page+1)*nbElt >= feed.length}, "Page suivante (>)")
			)
		)
	)
}