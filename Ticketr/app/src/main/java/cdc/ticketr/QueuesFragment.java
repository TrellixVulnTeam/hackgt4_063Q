package cdc.ticketr;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QueuesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QueuesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QueuesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public QueuesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QueuesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QueuesFragment newInstance(String param1, String param2) {
        QueuesFragment fragment = new QueuesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_queues, container, false);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://ticketr-api.herokuapp.com/user/queues?user=" +((MainActivity)getActivity()).user;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ((TextView) QueuesFragment.this.getView().findViewById(R.id.amountRemaining)).setText("" + 5);
                        if(response.equals("null"))
                            return;
                        Log.w("Help", response);
                        String[] ss = response.split("\"");
                        Log.w("Help2", ss.length + "");
                        if(ss.length == 0) {
                            return;
                        }
                        String[] companies = ss[1].split(",");
                        ArrayList<String> arr = new ArrayList();
                        for (String s : companies) {
                            Log.w("hbgv", s);
                            if (s.length() > 0)
                                arr.add(s);
                        }

                        ((TextView) QueuesFragment.this.getView().findViewById(R.id.amountRemaining)).setText("" + (5 - arr.size()));
                        LinearLayout QueueList = (LinearLayout) view.findViewById(R.id.QueueList);
                        for(int i = 0; i < arr.size(); i++) {
                            LinearLayout horizontal = new LinearLayout(getContext());

                            TextView name = new TextView(getContext());
                            name.setText(arr.get(i));
                            horizontal.addView(name);

                            ProgressBar progress = new ProgressBar(getContext(),null, android.R.attr.progressBarStyleHorizontal);
                            progress.setProgress(50);
                            horizontal.addView(progress);

                            QueueList.addView(horizontal);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Could get queues", Toast.LENGTH_SHORT).show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

        Button addQueue = (Button) view.findViewById(R.id.openShowcaserList);
        addQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment = new ShowcaserListFragment();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
              //      + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
