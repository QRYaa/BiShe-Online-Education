import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/oeChapter/page',
    params,
  });
}
export function save(data: any) {
  return request.post<any>({
    url: '/admin/oeChapter/save',
    data,
  });
}
export function update(data: any) {
  return request.put<any>({
    url: '/admin/oeChapter/update',
    data,
  });
}
export function get(id: any) {
  return request.get<any>({
    url: `/admin/oeChapter/get/${id}`,
  });
}
export function del(id: any) {
  return request.delete<any>({
    url: `/admin/oeChapter/delete/${id}`,
  });
}
export function batchDel(data: any) {
  return request.post<any>({
    url: '/admin/oeChapter/delete',
    data,
  });
}
