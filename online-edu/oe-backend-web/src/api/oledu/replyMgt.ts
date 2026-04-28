import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeReply/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeReply/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeReply/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeReply/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeReply/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeReply/delete',
    data,
  });
}
export function switchEnable(data: any) {
  return request.put<any>({
    url: '/admin/oeReply/switchEnable',
    data,
  });
}
