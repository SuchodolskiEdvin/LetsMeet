#!/usr/bin/env bash

# Skrypt pobiera tokena uwierzytelnienia do zooma

. ./env.sh
set -e

curl --location --request POST "https://zoom.us/oauth/token?grant_type=account_credentials&account_id=5xTVmAAHTd-TOLfofnsklA" \
--header 'Authorization: Basic bWdwSnVjYndUTHVtb0ZOVkt1TmhFZzoyYnk2akRxcHc3dGg1RnJJOHJJcnl6YWlrWU1keURpcg==' \
--header 'Cookie: __cf_bm=nPaxzJz0yqXBWesqfgVRedy2bml.eXkzlT9o8SISyCk-1705592688-1-ASSI8qBg9L6EPX9CZ+Ev61kq6g/C6dE4RIJUn57LMw4UZKOfsVwXBTtmhBjpHit6cKYxdvL4pXSjHaHHgThwKLU=; _zm_chtaid=907; _zm_ctaid=orsbU0-_ThO6GwChkTEqfg.1705592688760.4b173c9dffcd27d099830c53bdc4f9fa; _zm_mtk_guid=c54ab1f5036c4ea89ce03e9fd7152d41; _zm_page_auth=us05_c_p1aw1FWzSAOL1XDILXZHHA; _zm_ssid=us05_c_-vxlWgGwTZWfpLBVxaHumQ; cred=A09BEB02A7F79789F1CE10A3D7B5D4FF'